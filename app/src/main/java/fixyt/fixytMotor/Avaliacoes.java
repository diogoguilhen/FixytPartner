package fixyt.fixytMotor;



public class Avaliacoes {
    private String nota;
    private String flProcessado;


    public Avaliacoes(){

    }

    public Avaliacoes(String nota, String flProcessado) {
        this.nota = nota;
        this.flProcessado = flProcessado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getFlProcessado() {
        return flProcessado;
    }

    public void setFlProcessado(String flProcessado) {
        this.flProcessado = flProcessado;
    }
}
