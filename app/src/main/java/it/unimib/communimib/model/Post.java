package it.unimib.communimib.model;

public class Post {

    String pid;
    String titolo;

    String descrizione;

    User author;

    //todo inserire contatto


    public Post(String titolo, String descrizione, User author) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.author = author;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
