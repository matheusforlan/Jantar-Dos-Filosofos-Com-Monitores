package monitores;

public class Mesa {
	final static int PENSANDO = 1;
    final static int FAMINTO = 2;
    final static int COMENDO = 3;

    final static int QTD_FILOSOFOS = 6;
    // Eh importante definir o primeiro na ordem pois deadlocks são prevenidos.
    // 
    final static int PRIMEIRO_FILOSOFO = 0;
    final static int ULTIMO_FILOSOFO = QTD_FILOSOFOS - 1;

    // True = talher livre, False = Talher ocupado
    boolean[] talheres = new boolean[QTD_FILOSOFOS];
    int[] filosofos = new int[QTD_FILOSOFOS];
    // quantidade de tentativas do  filósofo  tentar comer.
    int[] tentativas = new int[QTD_FILOSOFOS];

    public Mesa() {
        for (int i = 0; i < QTD_FILOSOFOS; i++) {
            talheres[i] = true;
            filosofos[i] = PENSANDO;
            tentativas[i] = 0;
        }
    }

    public synchronized void pegarTalheres(int indice_filosofo) {
        filosofos[indice_filosofo] = FAMINTO;
        while (filosofos[filosofoDaEsquerda(indice_filosofo)] == COMENDO || filosofos[filosofoDaDireita(indice_filosofo)] == COMENDO) {
            try {
                tentativas[indice_filosofo]++;
                wait();
            } catch (InterruptedException e) {
                System.out.println("O filósofo morreu devido a starvation.");
            }
        }
        tentativas[indice_filosofo] = 0;
        talheres[talherEsquerdo(indice_filosofo)] = false;
        talheres[talherDireito(indice_filosofo)] = false;
        filosofos[indice_filosofo] = COMENDO;
        imprimeEstadosDosFilosofos();
        imprimeEstadoDosTalheres();
        imprimeTentativas();
    }

    public synchronized void soltarTalheres(int indice_filosofo) {
        talheres[talherEsquerdo(indice_filosofo)] = true;
        talheres[talherDireito(indice_filosofo)] = true;
        if (filosofos[filosofoDaEsquerda(indice_filosofo)] == FAMINTO || filosofos[filosofoDaDireita(indice_filosofo)] == FAMINTO) {
            notifyAll();
        }
        filosofos[indice_filosofo] = PENSANDO;
        imprimeEstadosDosFilosofos();
        imprimeEstadoDosTalheres();
        imprimeTentativas();
    }

    public int filosofoDaEsquerda(int indice_filosofo) {
        int esquerdo;
        if (indice_filosofo == PRIMEIRO_FILOSOFO) {
            esquerdo = ULTIMO_FILOSOFO;
        } else {
            esquerdo = indice_filosofo - 1;
        }
        return esquerdo;
    }

    public int filosofoDaDireita(int indice_filosofo) {
        int direito;
        if (indice_filosofo == ULTIMO_FILOSOFO) {
            direito = PRIMEIRO_FILOSOFO;
        } else {
            direito = indice_filosofo + 1;
        }
        return direito;
    }

    public int talherEsquerdo(int indice_filosofo) {
        int talherEsquerdo = indice_filosofo;
        return talherEsquerdo;
    }

    public int talherDireito(int indice_filosofo) {
        int talherDireito;
        if (indice_filosofo == ULTIMO_FILOSOFO) {
            talherDireito = 0;
        } else {
            talherDireito = indice_filosofo + 1;
        }
        return talherDireito;
    }

    private void imprimeEstadosDosFilosofos() {
        String estado = "-";
        System.out.print("Filósofos = [ ");
        for (int i = 0; i < QTD_FILOSOFOS; i++) {
            switch (filosofos[i]) {
                case PENSANDO:
                    estado = "PENSANDO";
                    break;
                case COMENDO:
                    estado = "COMENDO";
                    break;
                case FAMINTO:
                    estado = "FAMINTO";
                    break;
            }
            System.out.print(estado + " ");
        }
        System.out.println("]");
    }

    private void imprimeEstadoDosTalheres() {
        String estado = "-";
        System.out.print("Talheres = [ ");
        for (int i = 0; i < QTD_FILOSOFOS; i++) {
            if (talheres[i]) {
                estado = "LIVRE";
            } else {
                estado = "OCUPADO";
            }
            System.out.print(estado + " ");
        }
        System.out.println("]");
    }

    private void imprimeTentativas() {
        System.out.print("Tentou comer = [ ");
        for (int i = 0; i < QTD_FILOSOFOS; i++) {
            System.out.print(filosofos[i] + " ");
        }
        System.out.println("]\n");
    }
}
