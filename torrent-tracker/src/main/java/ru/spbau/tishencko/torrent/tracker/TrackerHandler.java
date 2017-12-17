package ru.spbau.tishencko.torrent.tracker;

import ru.spbau.tishencko.torrent.TorrentException;
import ru.spbau.tishencko.torrent.answer.tracker.Answer;
import ru.spbau.tishencko.torrent.query.tracker.*;
import ru.spbau.tishencko.torrent.entity.TrackerDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class TrackerHandler {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final TrackerDatabase tracker;
    private final Socket socket;

    public TrackerHandler(Socket socket, TrackerDatabase tracker) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        this.tracker = tracker;
    }

    public void execute() throws IOException, TorrentException {
        Query trackerQuery;
        try {
            trackerQuery = readQuery();
        } catch (EOFException e) {
            return;
        }

        Answer answer = trackerQuery.execute();
        answer.write(out);
    }

    private Query readQuery() throws IOException, TorrentException {
        byte type = in.readByte();
        switch (type) {
            case 1:
                return new ListQuery(tracker);
            case 2:
                UploadQuery upload = new UploadQuery(tracker);
                upload.read(in);
                return upload;
            case 3:
                SourcesQuery sources = new SourcesQuery(tracker);
                sources.read(in);
                return sources;
            case 4:
                UpdateQuery update = new UpdateQuery(tracker, socket.getInetAddress().getAddress());
                update.read(in);
                return update;
            default:
                throw new TorrentException("Unknown query type");
        }
    }
}
