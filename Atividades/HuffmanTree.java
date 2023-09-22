import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

class HuffmanNode { // Classe que representa um nó da árvore de Huffman
    int frequencia; // Frequência do caractere
    char caracter; // Caractere
    HuffmanNode esquerda; // Nó da esquerda
    HuffmanNode direita; // Nó da direita
}

class Comparador implements Comparator<HuffmanNode> { // Classe que compara dois nós da árvore de Huffman
    public int compare(HuffmanNode x, HuffmanNode y) { 
        return x.frequencia - y.frequencia; // Compara as frequências dos nós da árvore de Huffman do menor para o maior     
    }
}

public class HuffmanTree { // Classe que representa a árvore de Huffman

    public static void deleteTxtFiles() { // Método que deleta os arquivos .txt
        String[] files = { "copy.txt", "huffmanCode.txt", "comprimido.bin", "descomprimido.txt" }; // Vetor com os nomes dos arquivos .txt e .bin
        for (String file : files) { // Para cada arquivo no vetor
            File f = new File(file); // Cria um objeto File com o nome do arquivo
            if (f.exists()) { // Se o arquivo existir
                f.delete(); // Deleta o arquivo
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
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r"); // Lê o arquivo .txt

        HashMap<Character, Integer> map = new HashMap<>(); // Cria um HashMap

        String line = arq.readLine(); // Lê a linha do arquivo .txt que representa todo o arquivo .bin
        line = line.replaceAll(",", ""); // Remove as vírgulas da linha

        for (int i = 0; i < line.length(); i++) { // Para cada caractere da linha
            char c = line.charAt(i); // Pega o caractere
            if (map.containsKey(c)) { // Se o caractere já estiver no HashMap
                map.put(c, map.get(c) + 1); // Incrementa a frequência do caractere e coloca no HashMap
            } else { // Se o caractere não estiver no HashMap
                map.put(c, 1); // Coloca o caractere no HashMap com frequência 1
            }
        }

        arq.close();

        char first = map.keySet().iterator().next(); // Pega o primeiro caractere do HashMap
        map.remove(first); // Remove o primeiro caractere do HashMap (primeiro caractere é o número de registros do arquivo .bin)

        return map; // Retorna o HashMap
    }

    public static void printCode(HuffmanNode raiz, String code) throws IOException { // Método que cria o código de Huffman
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "rw"); // Cria um arquivo .txt

        if (raiz.esquerda == null && raiz.direita == null) { // Se o nó for uma folha
            arq.seek(arq.length()); // Vai para o final do arquivo .txt
            arq.writeChars(raiz.caracter + "=" + code); // Escreve o caractere e o código no arquivo .txt
            arq.writeChars("\n"); // Escreve uma quebra de linha no arquivo .txt
            arq.close();
            return; // Retorna para o método anterior 
        }

        printCode(raiz.esquerda, code + "0"); // Chama o método recursivamente para o nó da esquerda com o código concatenado com 0
        printCode(raiz.direita, code + "1"); // Chama o método recursivamente para o nó da direita com o código concatenado com 1
        arq.close();
    }

    public static void createTree(HashMap<Character, Integer> map) throws IOException { // Método que cria a árvore de Huffman
        int n = map.size(); // Pega o tamanho do HashMap
        char[] charArray = new char[n]; // Cria um vetor de caracteres com o tamanho do HashMap
        int[] charfreq = new int[n]; // Cria um vetor de inteiros com o tamanho do HashMap

        int count = 0; // Inicializa um contador 
        for (Character c : map.keySet()) { // Para cada caractere no HashMap
            charArray[count] = c; // Coloca o caractere no vetor de caracteres
            charfreq[count] = map.get(c); // Coloca a frequência do caractere no vetor de inteiros
            count++; // Incrementa o contador
        }

        PriorityQueue<HuffmanNode> q = new PriorityQueue<>(n, new Comparador()); // Cria uma fila de prioridade com o tamanho do HashMap e o comparador do menor para o maior

        for (int i = 0; i < n; i++) { // Para cada caractere no vetor de caracteres
            HuffmanNode hn = new HuffmanNode(); // Cria um nó de Huffman

            hn.caracter = charArray[i]; // Coloca o caractere no nó de Huffman
            hn.frequencia = charfreq[i]; // Coloca a frequência do caractere no nó de Huffman

            hn.esquerda = null; // Coloca o nó da esquerda como nulo
            hn.direita = null; // Coloca o nó da direita como nulo

            q.add(hn); // Adiciona o nó de Huffman na fila de prioridade
        }

        HuffmanNode raiz = null; // Cria um nó de Huffman raiz

        while (q.size() > 1) { // Enquanto a fila de prioridade tiver mais de um nó (enquanto a árvore não estiver completa)
            HuffmanNode x = q.peek(); // Pega o primeiro nó da fila de prioridade
            q.poll(); // Remove o primeiro nó da fila de prioridade
            HuffmanNode y = q.peek(); // Pega o primeiro nó da fila de prioridade
            q.poll(); // Remove o primeiro nó da fila de prioridade

            HuffmanNode f = new HuffmanNode(); // Cria um nó de Huffman
            f.frequencia = x.frequencia + y.frequencia; // Soma as frequências dos dois nós
            f.caracter = '-'; // Coloca o caractere do nó como hífen
            f.esquerda = x; // Coloca o nó da esquerda como o primeiro nó da fila de prioridade
            f.direita = y; // Coloca o nó da direita como o primeiro nó da fila de prioridade

            raiz = f; // Coloca o nó de Huffman como raiz
            q.add(f); // Adiciona o nó de Huffman na fila de prioridade
        }

        printCode(raiz, ""); // Chama o método que cria o código de Huffman
    }

    public static HashMap<Character, String> createCode() throws IOException { // Método que cria um HashMap com o caractere e o código de Huffman
        RandomAccessFile arq = new RandomAccessFile("huffmanCode.txt", "r"); // Abre o arquivo .txt com o código de Huffman

        HashMap<Character, String> code = new HashMap<Character, String>(); // Cria um HashMap com o caractere e o código de Huffman

        arq.seek(0); // Vai para o início do arquivo .txt
        while (arq.getFilePointer() < arq.length()) { // Enquanto não chegar no final do arquivo .txt
            String line = arq.readLine(); // Lê uma linha do arquivo .txt
            String[] split = line.split("="); // Separa a linha em um vetor de Strings
            code.put(split[0].charAt(1), split[1]); // Coloca o caractere e o código no HashMap
            split = null; // Limpa o vetor de Strings
        }

        arq.close();
        return code; // Retorna o HashMap
    }

    public static String removeMetaDados(String cod) { // Método que remove os metadados do código de Huffman contidos no HashMap
        String codSemMeta = ""; 

        for (int i = 0; i < cod.length(); i++) { // Para cada caractere no código de Huffman
            if (cod.charAt(i) == '1') { // Se o caractere for 1
                codSemMeta += '1'; // Adiciona 1 no código sem metadados
            } else if (cod.charAt(i) == '0') { // Se o caractere for 0
                codSemMeta += '0'; // Adiciona 0 no código sem metadados
            }
        }

        return codSemMeta; // Retorna o código sem metadados
    }

    public static int stringToBinary(String codigo) { // Método que converte o código de Huffman em binário
        int result = 0;

        for (int i = 0; i < codigo.length(); i++) { // Para cada caractere no código de Huffman
            if (codigo.charAt(i) == '1') { // Se o caractere for 1
                result += Math.pow(2, codigo.length() - i - 1); // Adiciona 2 elevado ao tamanho do código menos o índice do caractere menos 1 (para começar do 0) no resultado
            }
        }

        return result; // Retorna o resultado
    }

    public static void compress(HashMap<Character, String> code) throws IOException { // Método que compacta o arquivo
        RandomAccessFile arq = new RandomAccessFile("copy.txt", "r"); // Abre o arquivo .txt com o texto a ser compactado
        RandomAccessFile arq2 = new RandomAccessFile("comprimido.bin", "rw"); // Cria um arquivo .bin para armazenar o texto compactado

        arq.seek(0); // Vai para o início do arquivo .txt
        while (arq.getFilePointer() < arq.length()) { // Enquanto não chegar no final do arquivo .txt
            String line = arq.readLine(); // Lê uma linha do arquivo .txt

            for (int i = 0; i < line.length(); i++) { // Para cada caractere na linha
                char c = line.charAt(i); // Pega o caractere
                String cod = code.get(c); // Pega o código de Huffman do caractere
                if (cod != null) { // Se o código de Huffman não for nulo
                    String codigo = removeMetaDados(cod); // Chama o método que remove os metadados do código de Huffman
                    int bin = stringToBinary(codigo); // Chama o método que converte o código de Huffman em binário
                    arq2.write(bin); // Escreve o binário no arquivo .bin
                }
            }
        }

        arq.close();
        arq2.close();
    }

    public static boolean compactar(String fileName) throws IOException { // Método que compacta o arquivo
        long start = System.currentTimeMillis(); // Variável que armazena o tempo inicial

        deleteTxtFiles(); // Chama o método que deleta os arquivos .txt
        createTxt(fileName); // Chama o método que cria os arquivos .txt
        HashMap<Character, Integer> map = createHash(); // Define um HashMap com o caractere e a frequência
        createTree(map); // Chama o método que cria a árvore de Huffman

        HashMap<Character, String> huff = createCode(); // Define um HashMap com o caractere e o código de Huffman
        compress(huff); // Chama o método que compacta o arquivo

        File copia = new File("copy.txt"); // Abre o arquivo .txt com a cópia do texto 
        copia.delete(); // Deleta o arquivo .txt com a cópia do texto
        
        long time = (System.currentTimeMillis() - start); // Calcula o tempo de execução do programa em milissegundos
        float tamanhoOriginal = new File(fileName).length(); // Calcula o tamanho do arquivo original
        float tamanho = new File("comprimido.bin").length(); // Calcula o tamanho do arquivo compactado

        System.out.println("\nTempo de execução: " + time + " milissegundos"); // Imprime o tempo de execução do programa
        System.out.println("Tamanho do arquivo original: " + tamanhoOriginal + " bytes"); // Imprime o tamanho do arquivo original
        System.out.println("Tamanho do arquivo compactado: " + tamanho + " bytes"); // Imprime o tamanho do arquivo compactado
        System.out.println("Taxa de compressão: " + (tamanhoOriginal / tamanho) + " vezes"); // Imprime a taxa de compressão

        return true; // Retorna true
    }

    public static String removeZeroEsq(String codigo) { // Método que remove os zeros à esquerda do código de Huffman
        String cod = "";

        for (int i = 0; i < codigo.length(); i++) { // Para cada caractere no código de Huffman
            if (codigo.charAt(i) == '1') { // Se o caractere for 1
                cod = codigo.substring(i); // Pega o código a partir do índice do caractere
                break;
            }
        }

        return cod; // Retorna o código
    }

    public static boolean descompactar(String fileName) throws IOException { // Método que descompacta o arquivo
        long start = System.currentTimeMillis(); // Variável que armazena o tempo inicial

        HashMap<String, Character> map = new HashMap<String, Character>(); // Cria um HashMap com o código de Huffman e o caractere
        RandomAccessFile huff = new RandomAccessFile("huffmanCode.txt", "r"); // Abre o arquivo .txt com o código de Huffman

        huff.seek(0); // Vai para o início do arquivo .txt
        while (huff.getFilePointer() < huff.length()) { // Enquanto não chegar no final do arquivo .txt
            String line = huff.readLine(); // Lê uma linha do arquivo .txt
            String[] split = line.split("="); // Separa a linha em um vetor de Strings
            String codigo = split[1]; 
            codigo = removeZeroEsq(codigo); // Chama o método que remove os zeros à esquerda do código de Huffman
            char c = split[0].charAt(1); // Pega o caractere
            map.put(removeMetaDados(codigo), c); // Adiciona o código de Huffman e o caractere no HashMap sem os metadados
            split = null; // Limpa o vetor de Strings
        }

        huff.close();

        RandomAccessFile raf = new RandomAccessFile(fileName, "r"); // Abre o arquivo .bin com o texto compactado
        RandomAccessFile arq = new RandomAccessFile("descomprimido.txt", "rw"); // Cria um arquivo .txt para armazenar o texto descompactado
        if(arq.length() > 0) { // Se o arquivo .txt não estiver vazio
            arq.setLength(0); // Limpa o arquivo .txt
        }

        raf.seek(0); // Vai para o início do arquivo .bin
        while (raf.getFilePointer() < raf.length()) { // Enquanto não chegar no final do arquivo .bin
            int bin = raf.read(); // Lê um byte do arquivo .bin
            String codigo = Integer.toBinaryString(bin); // Converte o byte em binário
            codigo = removeMetaDados(codigo); // Chama o método que remove os metadados do código de Huffman
            if (map.get(codigo) != null) { // Se o código de Huffman não for nulo
                if(map.get(codigo) == ';') arq.writeChars("\n"); // Se o caractere for ;, escreve uma quebra de linha no arquivo .txt
                else arq.writeChar(map.get(codigo)); // Escreve o caractere no arquivo .txt
            }
        }

        arq.close();
        raf.close();

        long time = (System.currentTimeMillis() - start); // Calcula o tempo de execução do programa em milissegundos

        System.out.println("\nTempo de execução: " + time + " milissegundos"); // Imprime o tempo de execução do programa

        return true;
    }
}

