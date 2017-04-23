package fixyt.fixytMotor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CadastroMecanico implements Parcelable{

    // Tipo String
    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String senha;
    private String cpf;
    private String rg;
    private String sexo;
    private String tpLogradouro;
    private String endereco;
    private String cep;
    private String bairro;
    private String uf;
    private String cidade;
    private String pais;
    private String dataNascimento;

    // Cadastro particular do Perfil Mecanico
    private String perfilTipo;
    private String tipoServicoEmergencial;
    private String tipoServicoAgendado;
    private String servicos;

    //INSERCAO DA PARTE DE VARIAVEIS DA LOCALIZAÇÃO
    private String vLatitude;
    private String vLongitude;
    private String pontoReferencia;


    public CadastroMecanico() {
        super();
    }

    // Utilizando objetos como parcelável
    public CadastroMecanico(Parcel parcel){
        //Sobre o motorista
        this.nome=parcel.readString();
        this.sobrenome=parcel.readString();
        this.telefone=parcel.readString();
        this.email=parcel.readString();
        this.senha=parcel.readString();
        this.cpf=parcel.readString();
        this.rg=parcel.readString();
        this.sexo=parcel.readString();
        this.tpLogradouro=parcel.readString();
        this.endereco=parcel.readString();
        this.cep=parcel.readString();
        this.bairro=parcel.readString();
        this.uf=parcel.readString();
        this.cidade=parcel.readString();
        this.pais=parcel.readString();
        this.dataNascimento=parcel.readString();

        //Sobre o perfil do mecanico
        this.perfilTipo=parcel.readString();
        this.servicos=parcel.readString();
}

    public CadastroMecanico(String vLatitude, String vLongitude, String pontoReferencia) {
        this.vLatitude = vLatitude;
        this.vLongitude = vLongitude;
        this.pontoReferencia = pontoReferencia;
    }

    //Metodo de descrição de conteudo do Parcelable
    @Override
    public int describeContents(){
        return 0;
    }

    //Metodo de escrita para os campos da "parcela"
    @Override
    public void writeToParcel(Parcel parcel, int i){
        //Sobre o Motorista
        parcel.writeString(this.nome);
        parcel.writeString(this.sobrenome);
        parcel.writeString(this.telefone);
        parcel.writeString(this.email);
        parcel.writeString(this.senha);
        parcel.writeString(this.cpf);
        parcel.writeString(this.rg);
        parcel.writeString(this.sexo);
        parcel.writeString(this.tpLogradouro);
        parcel.writeString(this.endereco);
        parcel.writeString(this.cep);
        parcel.writeString(this.bairro);
        parcel.writeString(this.uf);
        parcel.writeString(this.cidade);
        parcel.writeString(this.pais);
        parcel.writeString(this.dataNascimento);

        //Sobre as informações do Mecanico
        parcel.writeString(this.perfilTipo);
        parcel.writeString(this.servicos);

    }

    public static final Creator<CadastroMecanico> CREATOR=new Creator<CadastroMecanico>() {
        @Override
        public CadastroMecanico createFromParcel(Parcel source) {
            return new CadastroMecanico(source);
        }

        @Override
        public CadastroMecanico[] newArray(int size) {
            return new CadastroMecanico[size];
        }
    };

    // CONSTRUTORES DAS TELAS  E MECANICOS SALVANDO POR TELAS

        // REGISTRAR 1
    public CadastroMecanico(String nome, String sobrenome, String telefone, String email, String senha) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    // REGISTRAR 2

    public CadastroMecanico(String nome, String sobrenome, String telefone, String email, String senha,String cpf, String rg, String sexo, String tpLogradouro, String endereco, String cep, String bairro, String uf, String cidade, String pais, String dataNascimento) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.rg = rg;
        this.sexo = sexo;
        this.tpLogradouro = tpLogradouro;
        this.endereco = endereco;
        this.cep = cep;
        this.bairro = bairro;
        this.uf = uf;
        this.cidade = cidade;
        this.pais = pais;
        this.dataNascimento = dataNascimento;
    }

    // REGISTRAR 3

    public CadastroMecanico(String perfilTipo, String tipoServicoEmergencial, String tipoServicoAgendado, String servicos) {
        this.perfilTipo = perfilTipo;
        this.tipoServicoEmergencial = tipoServicoEmergencial;
        this.tipoServicoAgendado = tipoServicoAgendado;
        this.servicos = servicos;
    }

    // GPS

    public CadastroMecanico(String vLatitude, String vLongitude) {
        this.vLatitude = vLatitude;
        this.vLongitude = vLongitude;
    }


// CADASTRO TOTAL

    public CadastroMecanico(String nome, String sobrenome, String telefone, String email, String senha, String cpf, String rg, String sexo, String tpLogradouro, String endereco, String cep, String bairro, String uf, String cidade, /*String pais,*/ String dataNascimento, String perfilTipo, /*String tipoServicoEmergencial, String tipoServicoAgendado,*/ String servicos) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.rg = rg;
        this.sexo = sexo;
        this.tpLogradouro = tpLogradouro;
        this.endereco = endereco;
        this.cep = cep;
        this.bairro = bairro;
        this.uf = uf;
        this.cidade = cidade;
        //this.pais = pais;
        this.dataNascimento = dataNascimento;
        this.perfilTipo = perfilTipo;
       // this.tipoServicoEmergencial = tipoServicoEmergencial;
        //this.tipoServicoAgendado = tipoServicoAgendado;
        this.servicos = servicos;
    }


    /// FIM DOS CONSTRUTORES DAS TELAS E MECANICOS SALVANDO PORTELAS


    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTipoServicoEmergencial() {
        return tipoServicoEmergencial;
    }

    public void setTipoServicoEmergencial(String tipoServicoEmergencial) {
        this.tipoServicoEmergencial = tipoServicoEmergencial;
    }

    public String getTipoServicoAgendado() {
        return tipoServicoAgendado;
    }

    public void setTipoServicoAgendado(String tipoServicoAgendado) {
        this.tipoServicoAgendado = tipoServicoAgendado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTpLogradouro() {
        return tpLogradouro;
    }

    public void setTpLogradouro(String tpLogradouro) {
        this.tpLogradouro = tpLogradouro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getPerfilTipo() {
        return perfilTipo;
    }

    public void setPerfilTipo(String perfilTipo) {
        this.perfilTipo = perfilTipo;
    }

    public String getServicos() {
        return servicos;
    }

    public void setServicos(ArrayList servicos)
    {


        this.servicos = servicos.toString();
    }


    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
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

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        pontoReferencia = pontoReferencia;
    }
}
