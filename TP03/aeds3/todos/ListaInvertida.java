package todos;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ListaInvertida {

    //Lista Invertida
    public static boolean listaInvertidaNome(RandomAccessFile raf, String nome) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertida.bin", "rw");
        if(lista.length() != 0){ 
            lista.setLength(0);
        }

        System.out.println("\n\tNome: " + nome);
        lista.writeUTF(nome);

        raf.seek(4);
        int contador = 0;
        while (raf.length() != raf.getFilePointer()) { 
            double pointer = raf.getFilePointer(); 
            if(raf.readByte() == 0) { 
                raf.readInt();  
                int id = raf.readInt();
                String n = raf.readUTF(); 
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
                if (n.equals(nome)) { 
                    System.out.println("\tID: " + id + " - Posição: " + (int) pointer); 
                    lista.writeInt(id); 
                    lista.writeInt((int)pointer); 
                    contador++;
                }
            }else { 
                raf.skipBytes(raf.readInt());
            }
           
        }

        System.out.println("\tQuantidade de registros: " + contador);
        lista.writeInt(contador);

        lista.close();
        return true;
    }

    public static boolean listaInvertidaCidade(RandomAccessFile raf, String cidade) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertida.bin", "rw");
        if(lista.length() != 0){ 
            lista.setLength(0);
        }

        System.out.println("\n\tCidade: " + cidade);
        lista.writeUTF(cidade);

        raf.seek(4); 
        int contador = 0; 
        while (raf.length() != raf.getFilePointer()) { 
            double pointer = raf.getFilePointer();
            if(raf.readByte() == 0) {  
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
                String c = raf.readUTF(); 
                raf.readInt(); 
                raf.readFloat(); 
                if (c.equals(cidade)) {
                    System.out.println("\tID: " + id + " - Posição: " + (int) pointer); 
                    lista.writeInt(id); 
                    lista.writeInt((int)pointer); 
                    contador++;
                }
            }else { 
                raf.skipBytes(raf.readInt());
            }
           
        }

        System.out.println("\tQuantidade de registros: " + contador);
        lista.writeInt(contador); 

        lista.close();
        return true;
    }
}

