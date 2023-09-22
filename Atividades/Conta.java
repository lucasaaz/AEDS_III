import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Conta {

    //Atributos
    private int idConta; 
    private String nomePessoa; 
    private int qtdEmails; 
    private String[] email; 
    private String nomeUsuario; 
    private String senha; 
    private String cpf;  
    private String cidade; 
    private int transferenciasRealizadas; 
    private float saldoConta; 

    //Construtores
    public Conta() {
        this.idConta = -1;
        this.nomePessoa = null;
        this.qtdEmails = 0;
        this.email = new String[qtdEmails];
        this.nomeUsuario = null;
        this.senha = null;
        this.cpf = null;
        this.cidade = null;
        this.transferenciasRealizadas = 0;
        this.saldoConta = -1;
    }

    public Conta(int idConta, String nomePessoa, int qtdEmails, String[] email, String nomeUsuario, String senha, String cpf, String cidade, int transferenciasRealizadas, float saldoConta) { // Construtor com parametros
        this.idConta = idConta;
        this.nomePessoa = nomePessoa;
        this.qtdEmails = qtdEmails;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.transferenciasRealizadas = transferenciasRealizadas;
        this.saldoConta = saldoConta;
    }
    
    //Get and Set

    public int getIdConta() {
		return idConta;
	}

	public void setIdConta(int idConta) {
		this.idConta = idConta;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public int getQtdEmails() {
		return qtdEmails;
	}

	public void setQtdEmails(int qtdEmails) {
		this.qtdEmails = qtdEmails;
	}

	public String[] getEmail() {
		return email;
	}

	public void setEmail(String[] email) {
		this.email = email;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
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

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public int getTransferenciasRealizadas() {
		return transferenciasRealizadas;
	}

	public void setTransferenciasRealizadas(int transferenciasRealizadas) {
		this.transferenciasRealizadas = transferenciasRealizadas;
	}

	public float getSaldoConta() {
		return saldoConta;
	}

	public void setSaldoConta(float saldoConta) {
		this.saldoConta = saldoConta;
	}
	

	
	
	
	

    public byte[] toByteArray() throws IOException { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos); 

        dos.writeInt(this.getIdConta()); 
        dos.writeUTF(this.getNomePessoa()); 
        dos.writeInt(this.getQtdEmails()); 
        for(int i = 0; i < this.getQtdEmails(); i++){ 
            dos.writeUTF(this.getEmail()[i]);
        }
        dos.writeUTF(this.getNomeUsuario());
        dos.writeUTF(this.getSenha()); 
        dos.writeUTF(this.getCpf()); 
        dos.writeUTF(this.getCidade()); 
        dos.writeInt(this.getTransferenciasRealizadas());
        dos.writeFloat(this.getSaldoConta()); 

        dos.close();
        baos.close();

        return baos.toByteArray();
    }

	public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba); 
        DataInputStream dis = new DataInputStream(bais); 
        this.idConta = dis.readInt(); 
        this.nomePessoa = dis.readUTF();
        this.qtdEmails = dis.readInt(); 
        for(int i = 0; i < this.getQtdEmails(); i++){ 
            this.email[i] = dis.readUTF();
        }
        this.nomeUsuario = dis.readUTF(); 
        this.senha = dis.readUTF(); 
        this.cpf = dis.readUTF();
        this.cidade = dis.readUTF(); 
        this.transferenciasRealizadas = dis.readInt(); 
        this.saldoConta = dis.readFloat(); 
    }

    public short size() throws IOException {
        return (short)this.toByteArray().length;
    }
}
