package ru.spbau.tishencko.torrent.client;

import ru.spbau.tishencko.torrent.TorrentException;
import ru.spbau.tishencko.torrent.answer.client.Answer;
import ru.spbau.tishencko.torrent.entity.Seed;
import ru.spbau.tishencko.torrent.query.client.GetQuery;
import ru.spbau.tishencko.torrent.query.client.Query;
import ru.spbau.tishencko.torrent.query.client.StatQuery;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class ClientHandler {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Seed seed;

    public ClientHandler(Socket socket, Seed seed) throws IOException {
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.seed = seed;
    }

    public void execute() throws IOException, TorrentException {
        Query clientQuery;
        try {
            clientQuery = readQuery();
        } catch (EOFException e) {
            return;
        }

        Answer answer = clientQuery.execute();
        answer.write(out);
    }

    private Query readQuery() throws IOException, TorrentException {
        byte type = in.readByte();
        switch (type) {
            case 1:
                StatQuery stat = new StatQuery(seed);
                stat.read(in);
                return stat;
            case 2:
                GetQuery get = new GetQuery(seed);
                get.read(in);
                return get;
            default:
                throw new TorrentException("Unknown query type");
        }
    }
}
