public class PersonalPage {

    private final int id;
    private boolean online;
    private final String name;
    private final String friend;
    private final String password;

    public PersonalPage(int id, String name, String password,
                        String friend, boolean online) {
        this.id = id;
        this.name = name;
        this.friend = friend;
        this.password = password;
        this.online = online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public int getId() {
        return this.id;
    }
    public boolean getOnline() {
        return this.online;
    }
    public String getName() {
        return this.name;
    }
    public String getFriend() {
        return this.friend;
    }
    public String getPassword(){
        String pass = "";
        for(int i = 0; i < this.password.length(); i++)
            pass += "*";
        return pass;
    }
    public String getPassAdmin(){
        return this.password;
    }

    public String toString(){
        return "Имя пользователя: " + this.getName() + "\nПароль пользователя: " + this.getPassword() +
        "\nId пользователя: " + this.getId() + "\n";
    }
    public String toStringAdmin(){
        return "Имя пользователя: " + this.getName() + "\nПароль пользователя: " + this.getPassAdmin() +
                "\nId пользователя: " + this.getId() + "\n";
    }
}