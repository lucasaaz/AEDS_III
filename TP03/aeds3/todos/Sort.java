package todos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Sort extends Crud {

    //Métodos
    public static void imprimirFile(RandomAccessFile raf) throws IOException { 
        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) {
            raf.readByte();
            raf.readInt();
            int id = raf.readInt();
            String nome = raf.readUTF();
            int qdtEmails = raf.readInt();
            String[] emails = new String[qdtEmails];
            for (int i = 0; i < qdtEmails; i++) {
                emails[i] = raf.readUTF();
            }
            raf.readUTF();
            raf.readUTF();
            raf.readUTF();
            raf.readUTF();
            raf.readInt();
            raf.readFloat();

            System.out.println("\tID: " + id + " \t| Nome: " + nome);
        }
    }

    public static Conta lerFile(RandomAccessFile raf, int pesquisa) { 
        try {
            Conta conta = new Conta();

            raf.seek(0); 
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
            System.out.println("\t Erro ao ler o arquivo!");
            return null;
        }
    }


    public static double lerFile2(RandomAccessFile raf, int pesquisa) throws IOException { 

        double num = 0;

        try {
            raf.seek(4);
            while (raf.length() != raf.getFilePointer()) { 

                num = raf.getFilePointer();
    
                if (raf.readByte() == 0) { 
                    raf.readInt(); 
                    int id = raf.readInt();
                    raf.readUTF(); 
                    int qdt = raf.readInt(); 
                    for (int i = 0; i < qdt; i++) {
                        raf.readUTF();
                    }
                    raf.readUTF(); 
                    raf.readUTF(); 
                    raf.readUTF(); 
                    raf.readUTF();
                    raf.readInt(); 
                    raf.readFloat();
    
                    if (id == pesquisa) { 
                        return num;
                    }
                } else {
                    raf.skipBytes(raf.readInt()); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 

        return num;
    }


    public static void deletarFile() { 
        File file1 = new File("arquivo1.bin");
        File file2 = new File("arquivo2.bin");
        File file3 = new File("arquivo3.bin");
        File file4 = new File("arquivo4.bin");
        File file5 = new File("arquivoFinal.bin");

        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
    }

    //Intercalação
    public static boolean intercalar(RandomAccessFile raf) throws IOException {

        deletarFile(); 

        System.out.println("\n\t Distribuindo");

        ArrayList<Conta> contas = new ArrayList<Conta>();
        Conta conta = new Conta();

        RandomAccessFile arq1 = new RandomAccessFile("arquivo1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arquivo2.bin", "rw");

        raf.seek(4); 
        while (raf.getFilePointer() < raf.length()) { 
            if (raf.readByte() == 0) {
                raf.readInt();
                conta = readById(raf, raf.readInt());
                contas.add(conta);
            } else {
                raf.skipBytes(raf.readInt());
            }
        }

        ArrayList<Conta> contasTmp = new ArrayList<Conta>();
        int contador = 0; 
        while (contas.size() > 0) { 
            for (int j = 0; j < 5; j++) { 
                if (contas.size() > 0) {
                    contasTmp.add(contas.get(0)); 
                    contas.remove(0); 
                }
            }

            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); 

            contador++;

            if (contador % 2 != 0) { 
                for (Conta c : contasTmp) {
                    arq1.writeByte(0);
                    arq1.writeInt(c.toByteArray().length);
                    arq1.write(c.toByteArray());
                }
            } else { 
                for (Conta c : contasTmp) {
                    arq2.writeByte(0);
                    arq2.writeInt(c.toByteArray().length);
                    arq2.write(c.toByteArray());
                }
            }

            contasTmp.clear();
        }

        ArrayList<Conta> contas1 = new ArrayList<Conta>();
        ArrayList<Conta> contas2 = new ArrayList<Conta>();

        System.out.println("\n\t Intercalação 1");

        arq1.seek(0); 
        while (arq1.getFilePointer() < arq1.length()) { 
            arq1.readByte();
            arq1.readInt();
            conta = lerFile(arq1, arq1.readInt()); 
            contas1.add(conta); 
        }

        arq2.seek(0); 
        while (arq2.getFilePointer() < arq2.length()) { 
            arq2.readByte();
            arq2.readInt();
            conta = lerFile(arq2, arq2.readInt()); 
            contas2.add(conta); 
        }

        RandomAccessFile arq3 = new RandomAccessFile("arquivo3.bin", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("arquivo4.bin", "rw");

        contador = 0; 
        contasTmp.clear(); 
        int m = 5; 

        while (contas1.size() > 0 || contas2.size() > 0) { 

            for (int i = 0; i < m; i++) {
                if (contas1.size() > 0) { 
                    contasTmp.add(contas1.get(0));
                    contas1.remove(0);
                }
                if (contas2.size() > 0) { 
                    contasTmp.add(contas2.get(0));
                    contas2.remove(0);
                }
            }

            contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); 

            contador++;

            if (contador % 2 != 0) {
                for (Conta c : contasTmp) {
                    arq3.writeByte(0);
                    arq3.writeInt(c.toByteArray().length);
                    arq3.write(c.toByteArray());
                }
            } else {
                for (Conta c : contasTmp) {
                    arq4.writeByte(0);
                    arq4.writeInt(c.toByteArray().length);
                    arq4.write(c.toByteArray());
                }
            }

            contasTmp.clear();
        }

        int qdt = 2; 
        while (arq2.length() > 0) { 

            System.out.println("\n\t Intercalação " + qdt + " ..."); 
            arq3.seek(0);
            while (arq3.getFilePointer() < arq3.length()) { 
                arq3.readByte();
                arq3.readInt();
                conta = lerFile(arq3, arq3.readInt());
                contas1.add(conta);
            }

            arq4.seek(0);
            while (arq4.getFilePointer() < arq4.length()) {
                arq4.readByte();
                arq4.readInt();
                conta = lerFile(arq4, arq4.readInt());
                contas2.add(conta);
            }

            arq1.setLength(0);
            arq2.setLength(0); 

            contador = 0;
            contasTmp.clear();
            m *= 2; 
            while (contas1.size() > 0 || contas2.size() > 0) { 
                for (int i = 0; i < m; i++) { 
                    if (contas1.size() > 0) { 
                        contasTmp.add(contas1.get(0));
                        contas1.remove(0);
                    }
                    if (contas2.size() > 0) { 
                        contasTmp.add(contas2.get(0));
                        contas2.remove(0);
                    }
                }

                contasTmp.sort((Conta c1, Conta c2) -> c1.getIdConta() - c2.getIdConta()); 

                contador++;

                if (contador % 2 != 0) { 
                    for (Conta c : contasTmp) {
                        arq1.writeByte(0);
                        arq1.writeInt(c.toByteArray().length);
                        arq1.write(c.toByteArray());
                    }
                } else { 
                    for (Conta c : contasTmp) {
                        arq2.writeByte(0);
                        arq2.writeInt(c.toByteArray().length);
                        arq2.write(c.toByteArray());
                    }
                }

                contasTmp.clear(); 
            }

            qdt++;
        }

  
        RandomAccessFile arqFinal = new RandomAccessFile("arquivoFinal.bin", "rw");
        arqFinal.seek(0); 
        arqFinal.writeInt(IDzao); 

        arqFinal.seek(4); 
        arq1.seek(0); 
        while (arq1.getFilePointer() < arq1.length()) {
            arq1.readByte(); 
            arq1.readInt(); 
            conta = lerFile(arq1, arq1.readInt()); 
            arqFinal.writeByte(0);
            arqFinal.writeInt(conta.toByteArray().length); 
            arqFinal.write(conta.toByteArray()); 
        }

        arq1.close(); 
        arq2.close();
        arq3.close();
        arq4.close();
        arqFinal.close();

        return true;
    }
}