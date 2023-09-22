import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


public class Menu extends Crud {

	//Tranferencia bancaria
    public static boolean tranferencia(RandomAccessFile raf, int id1, int id2, float valor){ 
        Conta conta1 = readById(raf, id1);
        Conta conta2 = readById(raf, id2);

        if(conta1 == null) System.out.println("\n\t Conta de origem não encontrada!"); 
        else if(conta2 == null) System.out.println("\n\t Conta de destino não encontrada!"); 
        else if(conta1.getSaldoConta() < valor) System.out.println("\n\t Saldo insuficiente!"); 
        else{ 
            conta1.setSaldoConta(conta1.getSaldoConta() - valor);
            conta2.setSaldoConta(conta2.getSaldoConta() + valor); 
            conta1.setTransferenciasRealizadas(conta1.getTransferenciasRealizadas() + 1);
            conta2.setTransferenciasRealizadas(conta2.getTransferenciasRealizadas() + 1); 
            update(raf, conta1); 
            update(raf, conta2);
            return true;
        }
        return false;
    }

    //Limpar tela
    public static void limparTela() throws InterruptedException, IOException{
        //Limpa a tela no windows, no linux e no MacOS
        if (System.getProperty("os.name").contains("Windows")){
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }else{
            Runtime.getRuntime().exec("clear");
        }
                
    }

    //Menu
    /**
     * @param args
     * @throws InterruptedException
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{ 
        RandomAccessFile raf = new RandomAccessFile("banco.bin", "rw");
        Scanner sc = new Scanner(System.in); 
        HashEstendido he = new HashEstendido();
        ArvoreBMais ar = new ArvoreBMais();
        Conta conta = new Conta();
        boolean status = false;
        
        int opcao = 0;
        boolean loop = true;

        if (raf.length() == 0) raf.writeInt(0);
        raf.seek(0);
        IDzao = raf.readInt(); 

        while(loop) { 
            System.out.println("\n\t ============= MENU =============\n");
            System.out.println("\t Criar uma conta bancária   -> [1]");
            System.out.println("\t realizar uma transferência -> [2]");
            System.out.println("\t Ler um registro (id)       -> [3]");
            System.out.println("\t Atualizar um registro      -> [4]");
            System.out.println("\t Deletar um registro (id)   -> [5]");
            System.out.println("\t Intercalação               -> [6]");
            System.out.println("\t Lista Invertida            -> [7]");
            System.out.println("\t Hash Estendido             -> [8]");
            System.out.println("\t ArvoreB Mais               -> [9]");
            System.out.println("\t Huffman                    -> [0]");
            System.out.println("\t Sair                       -> [10]");
            System.out.print("\n\t Escolha uma opção: ");
            do { 
                try {
                    opcao = sc.nextInt();
                    if(opcao < 0 || opcao > 9) System.out.print("\n\t Opção inválida!"); 
                } catch (Exception e) { 
                    System.out.print("\n\t Escolha uma opção: ");
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 9);

            switch (opcao) { 
                // Criar conta
                case 1:
                    limparTela();
                    conta = new Conta();
                    System.out.println("\n\t===== Inserir Dados do Usuário ===== ");
                    System.out.print("\t Nome: ");
                    conta.setNomePessoa(sc.next());
                    sc.nextLine();
                    
                    String in = "";
                    do {
                        System.out.print("\t CPF: ");
                        in = sc.next();
                    } while (in.length() != 11); 
                    conta.setCpf(in);

                    System.out.print("\t Cidade: ");
                    conta.setCidade(sc.next());
                    sc.nextLine();

                    System.out.print("\t Quantidade de e-mails: ");
                    conta.setQtdEmails(sc.nextInt());
                    String[] email = new String[conta.getQtdEmails()];
                    for(int i = 0; i < conta.getQtdEmails(); i++) {
                        System.out.print("\t -> E-mail " + (i + 1) + ": ");
                        email[i] = sc.next();
                    }
                    conta.setEmail(email);

                    if(raf.length() > 5) {  
                        do {
                            System.out.print("\t Usuário: ");
                            in = sc.next();
                        } while (readByUser(raf, in).getNomeUsuario().equals(in)); 
                        conta.setNomeUsuario(in);
                    }else {
                        System.out.print("\t Usuário: ");
                        in = sc.next();
                        conta.setNomeUsuario(in);
                    }
                    
                    System.out.print("\t Senha: ");
                    conta.setSenha(sc.next());

                    System.out.print("\t Saldo: ");
                    conta.setSaldoConta(sc.nextFloat());

                    conta.setIdConta(++IDzao); 

                    if(Criar(raf, conta)) System.out.println("\n\t Conta criada com sucesso!\n"); 
                    else System.out.println("\n\t Erro ao criar conta!\n");


                    he = new HashEstendido(4, "diretorio.hash.db", "cestos.hash.db");
                    ar = new ArvoreBMais(5, "arvoreb.db");

                    long dado = (long)(Sort.lerFile2(raf, Integer.valueOf(IDzao)));
                    int pos = (int)(Sort.lerFile2(raf, IDzao));

                    he.create(Integer.valueOf(IDzao), Long.valueOf(dado) );
                    ar.create(IDzao, pos);


                    break;
                // Transferencia bancaria
                case 2:
                    limparTela();
                	System.out.println("\n\t===== Transferencia Bancaria =====");
                    System.out.print("\t ID da conta que irá pagar: ");
                    int id1 = sc.nextInt();

                    System.out.print("\t ID da conta que irá receber: ");
                    int id2 = sc.nextInt();

                    System.out.print("\t Valor da transferência: ");
                    float valor = sc.nextFloat();

                    if(tranferencia(raf, id1, id2, valor)) System.out.println("\n\t Transferência realizada com sucesso!\n");
                    else System.out.println("\n\t Erro ao realizar transferência!\n");
                    
                    break;
                 // Ler um registro
                case 3: 
                    limparTela();
                    System.out.println("\n\t===== LER UM REGISTRO ===== ");

                    System.out.print("\t Digite o ID a ser pesquisado: ");
                    int pesquisa = sc.nextInt();

                    conta = readById(raf, pesquisa); 

                    if(conta == null) System.out.println("\n\t Conta não encontrada!\n");
                    else { 
                        System.out.println("\n\t Conta encontrada!\n");
                        System.out.println("\t ID: " + conta.getIdConta());
                        System.out.println("\t Nome: " + conta.getNomePessoa());
                        System.out.println("\t CPF: " + conta.getCpf());
                        System.out.println("\t Cidade: " + conta.getCidade());
                        System.out.println("\t E-mails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("\t -> " + conta.getEmail()[i]);
                        }
                        System.out.println("\t Usuário: " + conta.getNomeUsuario());
                        System.out.println("\t Senha: " + conta.getSenha());
                        System.out.println("\t Saldo: " + conta.getSaldoConta());
                        System.out.println("\t Transferências realizadas: " + conta.getTransferenciasRealizadas() + "\n");
                    }
                    break;
                // Atualizar um registro
                case 4: 
                    limparTela();
                	System.out.println("\n\t===== ATUALIZAR UM REGISTRO ===== ");
                    System.out.print("\t Digite o Usuário a ser pesquisado: ");
                    String user = sc.next();

                    conta = readByUser(raf, user); 

                    if(conta == null) System.out.println("\n\t Conta não encontrada!\n");
                    else {
                        System.out.println("\n\t Conta encontrada!\n");
                        System.out.println("\t ID: " + conta.getIdConta());
                        System.out.println("\t Nome: " + conta.getNomePessoa());
                        System.out.println("\t CPF: " + conta.getCpf());
                        System.out.println("\t Cidade: " + conta.getCidade());
                        System.out.println("\t E-mails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("\t -> " + conta.getEmail()[i]);
                        }
                        System.out.println("\t Usuário: " + conta.getNomeUsuario());
                        System.out.println("\t Senha: " + conta.getSenha());
                        System.out.println("\t Saldo: " + conta.getSaldoConta());
                        System.out.println("\t Transferências realizadas: " + conta.getTransferenciasRealizadas());

                        System.out.println("\n\t===== ATUALIZAR UM REGISTRO ===== ");
                        System.out.println("\t Qual campo deseja atualizar?");
                        System.out.println("\t 1 - Nome");
                        System.out.println("\t 2 - CPF");
                        System.out.println("\t 3 - Cidade");
                        System.out.println("\t 4 - E-mail");
                        System.out.println("\t 5 - Usuário");
                        System.out.println("\t 6 - Senha");
                        System.out.println("\t 7 - Saldo");
                        System.out.println("\t 8 - Cancelar");
                        System.out.print("\n\t Escolha uma opção: ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 8) System.out.println("\n\t Opção inválida!\n");
                            } catch (Exception e) {
                                System.out.print("\n\t Escolha uma opção: ");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 8); 

                        switch (opcao) { 
                            case 1:
                                System.out.print("\t Atualizar Nome: ");
                                conta.setNomePessoa(sc.next());
                                sc.nextLine();
                                break;
                            case 2: 
                                System.out.print("\t Atualizar CPF: ");
                                conta.setCpf(sc.next());
                                break;
                            case 3: 
                                System.out.print("\t Atualizar Cidade: ");
                                conta.setCidade(sc.next());
                                break;
                            case 4: 
                                System.out.print("\t Atualizar Quantidade de e-mails: ");
                                conta.setQtdEmails(sc.nextInt());
                                String[] email2 = new String[conta.getQtdEmails()];
                                for(int i = 0; i < conta.getQtdEmails(); i++) {
                                    System.out.print("\t -> Atualizar E-mail " + (i + 1) + ": ");
                                    email2[i] = sc.next();
                                }
                                conta.setEmail(email2);
                                break;
                            case 5: 
                                System.out.print("\t Atualizar Usuário: ");
                                conta.setNomeUsuario(sc.next());
                                break;
                            case 6:
                                System.out.print("\t Atualizar Senha: ");
                                conta.setSenha(sc.next());
                                break;
                            case 7: 
                                System.out.print("\t Atualizar Saldo: ");
                                conta.setSaldoConta(sc.nextFloat());
                                break;
                            case 8: 
                                System.out.println("\t Sair!\n");
                                break;
                        }

                        if(opcao != 8) { 
                            if(update(raf, conta)) System.out.println("\n\t Atualizado com sucesso!\n");
                            else System.out.println("\n\t Erro ao atualizar!\n");
                        }
                    }
                    break;
                // Deletar um registro
                case 5: 
                    limparTela();
                    System.out.println("\n\t===== DELETAR UM REGISTRO ===== ");
                    System.out.print("\t Digite um ID a ser pesquisado: ");
                    pesquisa = sc.nextInt();

                    conta = readById(raf, pesquisa);
                    if(conta == null) System.out.println("\n\t Conta não encontrada!\n");
                    else { 
                        System.out.println("\n\t Conta encontrada!\n");
                        System.out.println("\t ID: " + conta.getIdConta());
                        System.out.println("\t Nome: " + conta.getNomePessoa());
                        System.out.println("\t CPF: " + conta.getCpf());
                        System.out.println("\t Cidade: " + conta.getCidade());
                        System.out.println("\t E-mails: ");
                        for(int i = 0; i < conta.getQtdEmails(); i++) {
                            System.out.println("\t -> " + conta.getEmail()[i]);
                        }
                        System.out.println("\t Usuário: " + conta.getNomeUsuario());
                        System.out.println("\t Senha: " + conta.getSenha());
                        System.out.println("\t Saldo: " + conta.getSaldoConta());
                        System.out.println("\t Transferências realizadas: " + conta.getTransferenciasRealizadas());

                        System.out.println("\n\t CONFIRME, se deseja deletar essa conta?");
                        System.out.println("\t Sim -> [1]");
                        System.out.println("\t Não -> [2]");
                        System.out.print("\t Escolha uma opção: ");

                        do {
                            try {
                                opcao = sc.nextInt();
                                if(opcao < 1 || opcao > 2) System.out.println("\n\t Opção inválida!\n");
                            } catch (Exception e) {
                                System.out.print("\n\t Digite um número: ");
                                sc.next();
                                break;
                            }
                        } while (opcao < 1 || opcao > 2); 

                        if(opcao == 1) {
                            if(delete(raf, conta)){
                                try {
                                    ar = new ArvoreBMais(5, "arvoreb.db");
                                    pos = (int)(Sort.lerFile2(raf, IDzao));
                                    he.delete(pesquisa);
                                    ar.delete(pesquisa, pos);
                                    System.out.println("\n\t Deletado com sucesso!\n");
                                } catch (Exception e) {
                                    System.out.println("\n\t Erro ao deletar!\n");
                                }
                            } else System.out.println("\n\t Erro ao deletar!\n");
                        }
                        else System.out.println("\n\t Cancelado!\n");
                    }
                    break;
                 // Intercalacao de registros
                case 6: 
                    limparTela();
                    System.out.println("\n\t===== INTERCALAÇÃO ===== ");
                    System.out.println("\t Deseja realmente intercalar os registros?");
                    System.out.println("\t Sim -> [1]");
                    System.out.println("\t Não -> [2]");
                    System.out.print("\t Escola uma opção: ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 2) System.out.println("\n\t Opção inválida!\n");
                        } catch (Exception e) {
                            System.out.print("\n\t Digite um número: ");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 2); 

                    if(opcao == 1) {                         
                        if(Sort.intercalar(raf)) System.out.println("\n\t Intercalado com sucesso!\n");
                        else System.out.println("\n\t Erro ao intercalar!\n");
                        
                    }
                    else System.out.println("\n\t Cancelado!\n");

                    break;
                // Lista Invertida
                case 7:
                    limparTela();
                	System.out.println("\n\t===== LISTA INVERTIDA ===== ");
                    System.out.println("\t Nome     -> [1]");
                    System.out.println("\t Cidade   -> [2]");
                    System.out.println("\t Cancelar -> [3]");
                    System.out.print("\t Escolha uma opção: ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("\n\t Opção inválida!\n");
                        } catch (Exception e) {
                            System.out.print("\n\t Digite um número: ");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3); 

                    if(opcao == 1) {   
                        System.out.print("\n\t Digite o nome: ");
                        String name = sc.next();
                        if(ListaInvertida.listaInvertidaNome(raf, name)) System.out.println("\n\t Listado com sucesso!\n");
                        else System.out.println("\n\t Erro ao listar!\n");
                        
                    }else if(opcao == 2) { 
                        System.out.print("\n\t Digite a cidade: ");
                        String city = sc.next();
                        if(ListaInvertida.listaInvertidaCidade(raf, city)) System.out.println("\n\t Listado com sucesso!\n");
                        else System.out.println("\n\t Erro ao listar!\n");
                    }
                    else System.out.println("\n\t Cancelado!\n");
                    
                    break;
                //Hash Estendido
                case 8:
                    limparTela();
                    System.out.println("\n\t===== Hash Estendido ===== ");
                    System.out.println("\t Pesquisar (Id) -> [1]");
                    System.out.println("\t Mostrar Hash   -> [2]");
                    System.out.println("\t Cancelar       -> [3]");
                    System.out.print("\t Escolha uma opção: ");
                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("\n\t Opção inválida!\n");
                        } catch (Exception e) {
                            System.out.print("\n\t Digite um número: ");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3);

                    if(opcao == 1){
                        System.out.println("\n\t========== PESQUISA ==========");
                        System.out.print("\tChave (Id): ");
                        int chav = sc.nextInt();

                        Conta c = readById(raf, chav);
                        if(c == null){
                            System.out.println("\n\t Conta de origem não encontrada!");
                        }else{
                            try {
                                int chav2 = (int)(Sort.lerFile2(raf, chav));
                                System.out.println("\tPosição: "+chav2);
                                System.out.println("\n");
                            } catch (Exception e) {
                                System.out.println("\n\t Erro!!\n");
                            }
                        }

                    }else if(opcao == 2){
                        System.out.println("\n\t========== HASH ==========");
                        try {
                            he = new HashEstendido(4, "diretorio.hash.db", "cestos.hash.db");
        
                            pos = (int)(Sort.lerFile2(raf, IDzao));
        
                            he.read(pos);

                            he.print();
                        } catch (Exception e) {
                            System.out.println("\n\t Erro!\n");
                        }
                    }else if(opcao == 3){
                        limparTela();
                        break;
                    }else{
                        System.out.println("\n\t Erro!!\n");
                    }

                    break;
                case 9:
                    limparTela();
                    System.out.println("\n\t===== ArvoreB Mais ===== ");
                    System.out.println("\t Pesquisar (Id) -> [1]");
                    System.out.println("\t Mostrar Arvore -> [2]");
                    System.out.println("\t Cancelar       -> [3]");
                    System.out.print("\t Escolha uma opção: ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("\n\t Opção inválida!\n");
                        } catch (Exception e) {
                            System.out.print("\n\t Digite um número: ");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3);

                    if(opcao == 1){
                        System.out.println("\n\t========== PESQUISA ==========");
                        System.out.print("\tChave (Id): ");
                        int chav = sc.nextInt();

                        Conta c = readById(raf, chav);
                        if(c == null){
                            System.out.println("\n\t Conta de origem não encontrada!");
                        }else{
                            try {
                                int chav2 = (int)(Sort.lerFile2(raf, chav));
                                System.out.println("\tPosição: "+chav2);
                                System.out.println("\n");
                            } catch (Exception e) {
                                System.out.println("\n\t Erro!!\n");
                            }
                        }

                    }else if(opcao == 2){
                        System.out.println("\n\t========== ARVOREB+ ==========");
                        try {
                            ar = new ArvoreBMais(5, "arvoreb.db");

                            pos = (int)(Sort.lerFile2(raf, IDzao));

                            ar.read(pos);

                            ar.print();
                        } catch (Exception e) {
                            System.out.println("\n\t Erro!\n");
                        }
                    }else if(opcao == 3){
                        limparTela();
                        break; 
                    }else{
                        System.out.println("\n\t Erro!!\n");
                    }

                    break;
                case 0: // Huffman
                    System.out.println("\n\t========== HUFFMAN ==========");
                    System.out.println("\t Compactar    -> [1]");
                    System.out.println("\t Descompactar -> [2]");
                    System.out.println("\t Cancelar     -> [3]");
                    System.out.print("\t Escolha uma opção: ");

                    do {
                        try {
                            opcao = sc.nextInt();
                            if(opcao < 1 || opcao > 3) System.out.println("-> Opção inválida!");
                        } catch (Exception e) {
                            System.out.println("-> Digite um número!");
                            sc.next();
                            break;
                        }
                    } while (opcao < 1 || opcao > 3); // Enquanto a opção for inválida continua no loop

                    if(opcao == 1) { // Se a opção for 1, compacta o arquivo
                        String name = "banco.bin";
                        if(HuffmanTree.compactar(name)) System.out.println("\n-> Compactado com sucesso!\n");
                        else System.out.println("\n-> Erro ao compactar!");
                    }else if(opcao == 2) { // Se a opção for 2, descompacta o arquivo
                        String name = "comprimido.bin";
                        if(HuffmanTree.descompactar(name)) System.out.println("\n-> Descompactado com sucesso!\n");
                        else System.out.println("\n-> Erro ao descompactar!");
                    }
                    else System.out.println("-> Cancelado!\n");
                    
                    break;
                // Sair
                case 10: 
                    limparTela();
                    System.out.println("\n\n\t Você deixou o programa!\n\n");
                    loop = false;
                    loop = false;
                    break;
            }
        }

        sc.close();
        raf.close();
    }
}

