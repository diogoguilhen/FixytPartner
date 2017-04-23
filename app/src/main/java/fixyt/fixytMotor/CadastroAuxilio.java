package fixyt.fixytMotor;


public class CadastroAuxilio {
    //INSERCAO DA PARTE DE VARIAVEIS DA LOCALIZAÇÃO
    private String vLatitude;
    private String vLongitude;
    private String vOnline;
    private String vServico;
    private String vEmAtendimento;

    //Construtores

    public CadastroAuxilio(){ super();}

    public CadastroAuxilio(String vLatitude, String vLongitude, String vOnline, String vServico, String vEmAtendimento) {
        this.vLatitude = vLatitude;
        this.vLongitude = vLongitude;
        this.vOnline = vOnline;
        this.vServico = vServico;
        this.vEmAtendimento = vEmAtendimento;
    }

    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getvOnline() {
        return vOnline;
    }

    public void setvOnline(String vOnline) {
        this.vOnline = vOnline;
    }

    public String getvServico() {
        return vServico;
    }

    public void setvServico(String vServico) {
        this.vServico = vServico;
    }

    public String getvEmAtendimento() {
        return vEmAtendimento;
    }

    public void setvEmAtendimento(String vEmAtendimento) {
        this.vEmAtendimento = vEmAtendimento;
    }
}
