package broadcast.general.message.type;

import java.util.Set;

public class Message {
    private String date;
    private String name;
    private String message;
    private Set<String> clients;

    public Set<String> getClients() {
        return clients;
    }

    public void setClients(Set<String> clients) {
        this.clients = clients;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
