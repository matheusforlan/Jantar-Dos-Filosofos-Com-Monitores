package monitores;

public class Jantar {
	
	    public static void main(String[] args) {
	        Mesa mesa = new Mesa();
	        
	        for (int filosofo = 0; filosofo < Mesa.QTD_FILOSOFOS; filosofo++) {
	            new Filosofo( mesa, filosofo).start();
	        }
	    }
}
