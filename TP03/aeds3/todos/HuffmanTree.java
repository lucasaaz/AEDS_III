package todos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

class HuffmanNode { // Classe que representa um nó da árvore de Huffman
    int frequencia; 
    char caracter; 
    HuffmanNode esquerda; 
    HuffmanNode direita; 
}

class Comparador implements Comparator<HuffmanNode> { // Classe que compara dois nós da árvore de Huffman
    public int compare(HuffmanNode x, HuffmanNode y) { 
        return x.frequencia - y.frequencia; // Compara as frequências dos nós da árvore de Huffman do menor para o maior     
    }
}

public class HuffmanTree { // Classe que representa a árvore de Huffman

    public static void deleteTxtFiles() { 
        String[] files = { "copy.txt", "huffmanCode.txt", "comprimido.bin", "descomprimido.txt" }; 
        for (String file : files) { 
            File f = new File(file); 
            if (f.exists()) { 
                f.delete(); 
            }
        }
    }

    public static void createTxt(String fileName) throws IOException { // Método que cria um arquivo .txt
        RandomAccessFile raf = new RandomAccessFile(fileName, "r"); // Lê o arquivo .bin
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "rw"); // Cria um arquivo .txt

        String[] emails; 

        // Lê o arquivo .bin e escreve no arquivo .txt elemento por elemento
        raf.seek(0);
        int lastId = raf.readInt();
        arq.writeChars(Integer.toString(lastId));
        arq.writeChar(',');
        while (raf.getFilePointer() < raf.length()) {
            byte lapide = raf.readByte();
            arq.writeChars(Byte.toString(lapide));
            arq.writeChar(',');
            int tam = raf.readInt();
            arq.writeChars(Integer.toString(tam));
            arq.writeChar(',');
            int id = raf.readInt();
            arq.writeChars(Integer.toString(id));
            arq.writeChar(',');
            String nome = raf.readUTF();
            arq.writeChars(nome);
            arq.writeChar(',');
            int qtdEmails = raf.readInt();
            arq.writeChars(Integer.toString(qtdEmails));
            arq.writeChar(',');
            emails = new String[qtdEmails];
            for (int i = 0; i < qtdEmails; i++) {
                emails[i] = raf.readUTF();
                arq.writeChars(emails[i]);
                arq.writeChar(',');
            }
            String usuario = raf.readUTF();
            arq.writeChars(usuario);
            arq.writeChar(',');
            String senha = raf.readUTF();
            arq.writeChars(senha);
            arq.writeChar(',');
            String cpf = raf.readUTF();
            arq.writeChars(cpf);
            arq.writeChar(',');
            String cidade = raf.readUTF();
            arq.writeChars(cidade);
            arq.writeChar(',');
            int tranferencias = raf.readInt();
            arq.writeChars(Integer.toString(tranferencias));
            arq.writeChar(',');
            float saldo = raf.readFloat();
            arq.writeChars(Float.toString(saldo));
            arq.writeChar(',');
            arq.writeChars(";");
        }

        raf.close();
        arq.close();
    }

    public static HashMap<Character, Integer> createHash() throws IOException { // Método que cria um HashMap com os caracteres e suas frequências
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r"); 

        HashMap<Character, Integer> map = new HashMap<>(); 

        String line = arq.readLine(); 
        line = line.replaceAll(",", ""); 

        for (int i = 0; i < line.length(); i++) { 
            char c = line.charAt(i); 
            if (map.containsKey(c)) { 
                map.put(c, map.get(c) + 1); 
            } else { 
                map.put(c, 1); 
            }
        }

        arq.close();

        char first = map.keySet().iterator().next(); 
        map.remove(first); 

        return map; 
    }

    public static void printCode(HuffmanNode raiz, String code) throws IOException { // Método que cria o código de Huffman
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "rw"); 

        if (raiz.esquerda == null && raiz.direita == null) { 
            arq.seek(arq.length()); 
            arq.writeChars(raiz.caracter + "=" + code); 
            arq.writeChars("\n"); 
            arq.close();
            return; 
        }

        printCode(raiz.esquerda, code + "0"); 
        printCode(raiz.direita, code + "1"); 
        arq.close();
    }

    public static void createTree(HashMap<Character, Integer> map) throws IOException { // Método que cria a árvore de Huffman
        int n = map.size(); 
        char[] charArray = new char[n]; 
        int[] charfreq = new int[n]; 

        int count = 0; 
        for (Character c : map.keySet()) { 
            charArray[count] = c; 
            charfreq[count] = map.get(c); 
            count++; 
        }

        PriorityQueue<HuffmanNode> q = new PriorityQueue<>(n, new Comparador());

        for (int i = 0; i < n; i++) { 
            HuffmanNode hn = new HuffmanNode(); 

            hn.caracter = charArray[i]; 
            hn.frequencia = charfreq[i]; 

            hn.esquerda = null; 
            hn.direita = null;

            q.add(hn); 
        }

        HuffmanNode raiz = null;

        while (q.size() > 1) { 
            HuffmanNode x = q.peek(); 
            q.poll(); 
            HuffmanNode y = q.peek(); 
            q.poll(); 

            HuffmanNode f = new HuffmanNode(); 
            f.frequencia = x.frequencia + y.frequencia; 
            f.caracter = '-'; 
            f.esquerda = x;
            f.direita = y; 

            raiz = f; 
            q.add(f); 
        }

        printCode(raiz, ""); 
    }

    public static HashMap<Character, String> createCode() throws IOException { // Método que cria um HashMap com o caractere e o código de Huffman
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "r"); 

        HashMap<Character, String> code = new HashMap<Character, String>(); 

        arq.seek(0); 
        while (arq.getFilePointer() < arq.length()) { 
            String line = arq.readLine(); 
            String[] split = line.split("="); 
            code.put(split[0].charAt(1), split[1]);
            split = null;
        }

        arq.close();
        return code; 
    }

    public static String removeMetaDados(String cod) { // Método que remove os metadados do código de Huffman contidos no HashMap
        String codSemMeta = ""; 

        for (int i = 0; i < cod.length(); i++) { 
            if (cod.charAt(i) == '1') { 
                codSemMeta += '1';
            } else if (cod.charAt(i) == '0') { 
                codSemMeta += '0'; 
            }
        }

        return codSemMeta; // Retorna o código sem metadados
    }

    public static int stringToBinary(String codigo) {
        int result = 0;

        for (int i = 0; i < codigo.length(); i++) { 
            if (codigo.charAt(i) == '1') { 
                result += Math.pow(2, codigo.length() - i - 1); 
            }
        }

        return result; // Retorna o resultado
    }

    public static void compress(HashMap<Character, String> code) throws IOException { // Método que compacta o arquivo
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r"); 
        RandomAccessFile arq2 = new RandomAccessFile("comprimido.bin", "rw"); 

        arq.seek(0);
        while (arq.getFilePointer() < arq.length()) { 
            String line = arq.readLine(); 

            for (int i = 0; i < line.length(); i++) { 
                char c = line.charAt(i); 
                String cod = code.get(c); 
                if (cod != null) { 
                    String codigo = removeMetaDados(cod); 
                    int bin = stringToBinary(codigo); 
                    arq2.write(bin); 
                }
            }
        }

        arq.close();
        arq2.close();
    }

    public static boolean compactar(String fileName) throws IOException { // Método que compacta o arquivo
        long start = System.currentTimeMillis(); 

        deleteTxtFiles(); 
        createTxt(fileName); 
        HashMap<Character, Integer> map = createHash(); 
        createTree(map);

        HashMap<Character, String> huff = createCode(); 
        compress(huff); 

        File copia = new File("copy.txt");
        copia.delete();
        
        long time = (System.currentTimeMillis() - start); 
        float tamanhoOriginal = new File(fileName).length();
        float tamanho = new File("comprimido.bin").length(); 

        System.out.println("\nTempo de execução: " + time + " milissegundos"); 
        System.out.println("Tamanho do arquivo original: " + tamanhoOriginal + " bytes"); 
        System.out.println("Tamanho do arquivo compactado: " + tamanho + " bytes");
        System.out.println("Taxa de compressão: " + (tamanhoOriginal / tamanho) + " vezes"); 

        return true; 
    }

    public static String removeZeroEsq(String codigo) { // Método que remove os zeros à esquerda do código de Huffman
        String cod = "";

        for (int i = 0; i < codigo.length(); i++) {
            if (codigo.charAt(i) == '1') { 
                cod = codigo.substring(i); 
                break;
            }
        }

        return cod;
    }

    public static boolean descompactar(String fileName) throws IOException { // Método que descompacta o arquivo
        long start = System.currentTimeMillis(); 

        HashMap<String, Character> map = new HashMap<String, Character>(); 
        RandomAccessFile huff = new RandomAccessFile("huffmanCode.txt", "r"); 
        huff.seek(0); 
        while (huff.getFilePointer() < huff.length()) { 
            String line = huff.readLine(); 
            String[] split = line.split("=");
            String codigo = split[1]; 
            codigo = removeZeroEsq(codigo); 
            char c = split[0].charAt(1); 
            map.put(removeMetaDados(codigo), c); 
            split = null; 
        }

        huff.close();

        RandomAccessFile raf = new RandomAccessFile(fileName, "r"); 
        RandomAccessFile arq = new RandomAccessFile("descomprimido.txt", "rw"); 
        if(arq.length() > 0) { 
            arq.setLength(0); 
        }

        raf.seek(0);
        while (raf.getFilePointer() < raf.length()) { 
            int bin = raf.read(); 
            String codigo = Integer.toBinaryString(bin); 
            codigo = removeMetaDados(codigo);
            if (map.get(codigo) != null) { 
                if(map.get(codigo) == ';') arq.writeChars("\n"); 
                else arq.writeChar(map.get(codigo)); 
            }
        }

        arq.close();
        raf.close();

        long time = (System.currentTimeMillis() - start); 

        System.out.println("\nTempo de execução: " + time + " milissegundos"); 

        return true;
    }
}


