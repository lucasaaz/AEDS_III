import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.nio.file.Path;

public class Crud {

    static int IDzao;
    public static String DATABASE;
    private Path arq;

    public Crud() {
        String root = "";
            root = arq.toString().endsWith(".jar")
                    ? arq.toString().substring(0, arq.toString().lastIndexOf(File.separator))
                    : arq.toString();
        Crud.DATABASE = root + "" + File.separator + "db" + File.separator + "conta_banco.db";
    }

    //Criar conta
    public static boolean Criar(RandomAccessFile raf, Conta conta) throws IOException { 

        try {
            raf.seek(raf.length());
            raf.writeByte(0); 
            raf.writeInt(conta.toByteArray().length); 
            raf.writeInt(conta.getIdConta());
            raf.writeUTF(conta.getNomePessoa());
            raf.writeInt(conta.getQtdEmails()); 
            for (int i = 0; i < conta.getQtdEmails(); i++) { 
                raf.writeUTF(conta.getEmail()[i]); 
            }
            raf.writeUTF(conta.getNomeUsuario());
            raf.writeUTF(conta.getSenha()); 
            raf.writeUTF(conta.getCpf()); 
            raf.writeUTF(conta.getCidade()); 
            raf.writeInt(conta.getTransferenciasRealizadas()); 
            raf.writeFloat(conta.getSaldoConta()); 

            raf.seek(0); 
            raf.writeInt(IDzao); 

            return true;
        } catch (Exception e) {
            System.out.println("\tErro ao criar registro!");
            return false;
        }
    }

    //Ler conta (id)
    public static Conta readById(RandomAccessFile raf, int pesquisa) { 
        try {
            Conta conta = new Conta();

            raf.seek(4); 
            while (raf.getFilePointer() < raf.length()) { 
                if (raf.readByte() == 0) { 
                    int tam = raf.readInt();

                    conta.setIdConta(raf.readInt()); 

                    if (conta.getIdConta() == pesquisa) { 
                        conta.setNomePessoa(raf.readUTF());
                        conta.setQtdEmails(raf.readInt());
                        String[] emails = new String[conta.getQtdEmails()];
                        for (int i = 0; i < conta.getQtdEmails(); i++) {
                            emails[i] = raf.readUTF();
                        }
                        conta.setEmail(emails);
                        conta.setNomeUsuario(raf.readUTF());
                        conta.setSenha(raf.readUTF());
                        conta.setCpf(raf.readUTF());
                        conta.setCidade(raf.readUTF());
                        conta.setTransferenciasRealizadas(raf.readInt());
                        conta.setSaldoConta(raf.readFloat());

                        return conta;
                    } else {
                        raf.skipBytes(tam - 4);
                    }
                } else {
                    raf.skipBytes(raf.readInt());
                }
            }

            return null;
        } catch (Exception e) {
            System.out.println("\nErro ao ler registro!");
            return null;
        }
    }

  //Ler conta (nome de usuario)
    public static Conta readByUser(RandomAccessFile raf, String pesquisa) { 
        try {
            Conta conta = null; 
            String[] emails; 
            boolean achado = false;

            raf.seek(4); 
            while (raf.getFilePointer() < raf.length() && !achado) {
                if (raf.readByte() == 0) { //0 é ativo
                    conta = new Conta(); 
                    raf.readInt();

                    conta.setIdConta(raf.readInt());
                    conta.setNomePessoa(raf.readUTF());
                    conta.setQtdEmails(raf.readInt());
                    emails = new String[conta.getQtdEmails()];
                    for (int i = 0; i < conta.getQtdEmails(); i++) {
                        emails[i] = raf.readUTF();
                    }
                    conta.setEmail(emails);
                    conta.setNomeUsuario(raf.readUTF());
                    conta.setSenha(raf.readUTF());
                    conta.setCpf(raf.readUTF());
                    conta.setCidade(raf.readUTF());
                    conta.setTransferenciasRealizadas(raf.readInt());
                    conta.setSaldoConta(raf.readFloat());

                    if(conta.getNomeUsuario().equals(pesquisa)){ 
                        achado = true;
                    }
                } else {
                    raf.skipBytes(raf.readInt());
                }
            }

            return conta;
        } catch (Exception e) {
            System.out.println("\tErro ao ler registro!");
            return null;
        }
    }

    //Atualizar conta
    public static boolean update(RandomAccessFile raf, Conta conta) { 
        try {
            raf.seek(4);
            while (raf.getFilePointer() < raf.length()) { 
                if (raf.readByte() == 0) { 
                    int tam = raf.readInt();

                    if (raf.readInt() == conta.getIdConta()) {
                        if (tam >= conta.toByteArray().length) { 

                            raf.writeUTF(conta.getNomePessoa());
                            raf.writeInt(conta.getQtdEmails());
                            for (int i = 0; i < conta.getQtdEmails(); i++) {
                                raf.writeUTF(conta.getEmail()[i]);
                            }
                            raf.writeUTF(conta.getNomeUsuario());
                            raf.writeUTF(conta.getSenha());
                            raf.writeUTF(conta.getCpf());
                            raf.writeUTF(conta.getCidade());
                            raf.writeInt(conta.getTransferenciasRealizadas());
                            raf.writeFloat(conta.getSaldoConta());

                            return true;
                        } else { 
                            raf.seek(raf.getFilePointer() - 9); 
                            raf.writeByte(1); 
                            return Criar(raf, conta);
                        }
                    } else {
                        raf.skipBytes(tam - 4);
                    }
                } else {
                    raf.skipBytes(raf.readInt());
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("\tErro ao atualizar registro!");
            return false;
        }
    }

    //Deletar conta
    public static boolean delete(RandomAccessFile raf, Conta conta) {
        try {
            raf.seek(4);
            while(raf.getFilePointer() < raf.length()) { 
                if(raf.readByte() == 0) { 
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == conta.getIdConta()) { 
                        raf.seek(raf.getFilePointer() - 9); 
                        raf.writeByte(1); 
                        return true;
                    }else {
                        raf.skipBytes(tam - 4);
                    }
                }else {
                    raf.skipBytes(raf.readInt()); 
                }
            }
            return false;
        }catch (Exception e) {
            System.out.println("\tErro ao deletar registro!");
            return false;
        }
    }
}