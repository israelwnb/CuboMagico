import java.util.ArrayList;

public class AlgebraLinear {
	
	static Vetor transformacaoLinear(double[][] matriz, Vetor ponto)
	{
		Vetor aux = new Vetor(matriz[0][0]*ponto.getX() + matriz[0][1]*ponto.getY() + matriz[0][2]*ponto.getZ(), matriz[1][0]*ponto.getX() + matriz[1][1]*ponto.getY() + matriz[1][2]*ponto.getZ(), matriz[2][0]*ponto.getX() + matriz[2][1]*ponto.getY() + matriz[2][2]*ponto.getZ());
		return aux;
	}

	//Calcula a norma de um vetor
		static double norma(Vetor vetor)
		{
			double aux = vetor.getX()*vetor.getX() + vetor.getY()*vetor.getY() + vetor.getZ()*vetor.getZ();
			return Math.sqrt(aux);
		}
		
		//Divide um vetor por um escalar
		static Vetor divisaoPorEscalar(Vetor vetor, double escalar)
		{
		    Vetor aux = new Vetor(vetor.getX()/escalar,(vetor.getY()/escalar),vetor.getZ()/escalar);
		    return aux;
		}

		//Mutiplica um vetor por um escalar
		static Vetor multiplicacaoPorEscalar(Vetor vetor, double escalar)
		{
		    Vetor aux = new Vetor(vetor.getX()*escalar,(vetor.getY()*escalar),vetor.getZ()*escalar);
		    return aux;
		}

		//Retorna um valor t, que subistitui a componente de menor valor do valor de entrada por 1
		static Vetor chutarT(Vetor vetor){
		    double aux[] = {Math.abs(vetor.getY()), Math.abs(vetor.getZ())};
		    double temp = vetor.getX();
		    int maximo = -1;

		    for (int i = 0; i < 2; i++){
		        if (aux[i] < temp){
		            temp = aux[i];
		            maximo = i;
		        }
		    }

		    switch(maximo)
		    {
		    case -1:
		        {
		            Vetor res = new Vetor(1,vetor.getY(),vetor.getZ());
		            return res;
		        }
		    case 0:
		        {
		            Vetor res = new Vetor(vetor.getX(),1,vetor.getZ());
		            return res;
		        }
		    case 1:
		        {
		            Vetor res = new Vetor(vetor.getX(),vetor.getY(),1);
		            return res;
		        }
		    default:
		        System.out.println("Erro na geração do t!");
		        return null;
		    }
		    
		}
		
		//Retorna  o produto dos dois vetores de entrada
		static double produtoEscalar(Vetor vet1, Vetor vet2)
		{
		    double aux = vet1.getX()*vet2.getX() + vet1.getY()*vet2.getY() + vet1.getZ()*vet2.getZ();

		    return aux;
		}

		//Retorna um vetor perpendicular aos dois vetores de entrada
		
		static Vetor produtoVetorial(Vetor vet1, Vetor vet2)
		{
		    double auxX = vet1.getY()*vet2.getZ() - vet1.getZ()*vet2.getY();
		    double auxY = vet1.getZ()*vet2.getX() - vet1.getX()*vet2.getZ();
		    double auxZ = vet1.getX()*vet2.getY() - vet1.getY()*vet2.getX();

		    Vetor aux = new Vetor(auxX, auxY, auxZ);

		    return aux;
		}
		
		//Retorna o total da adição de dois vetores
		static Vetor somaVetorial(Vetor vet1, Vetor vet2)
		{
		    double auxX = vet1.getX()+vet2.getX();
		    double auxY = vet1.getY()+vet2.getY();
		    double auxZ = vet1.getZ()+vet2.getZ();

		    Vetor aux = new Vetor(auxX, auxY, auxZ);

		    return aux;
		}

		//Retorna a diferença da subtração de dois vetores
		static Vetor subtracaoVetorial(Vetor vet1, Vetor vet2)
		{
		    double auxX = vet1.getX()-vet2.getX();
		    double auxY = vet1.getY()-vet2.getY();
		    double auxZ = vet1.getZ()-vet2.getZ();

		    Vetor aux = new Vetor(auxX, auxY, auxZ);

		    return aux;
		}
		
		static Vetor getNormal(Poligono pol)
		{
		    ArrayList<Vetor> aux = pol.getVertices();
		    Vetor soma = new Vetor(0,0,0);

		    for (int i = 0; i < aux.size(); i++)
		    {
		        if (i == aux.size() - 1)
		        {
		        	soma = somaVetorial(soma,produtoVetorial(aux.get(i),aux.get(0)));
		        }
		        else
		        {
		        	soma = somaVetorial(soma,produtoVetorial(aux.get(i),aux.get(i+1)));
		        }
		    }

		    return divisaoPorEscalar(soma,norma(soma));
		}

		static Vetor normal(Vetor p, Vetor q, Vetor r)
		{
		    Vetor soma = new Vetor(0,0,0);
		    soma = somaVetorial(somaVetorial(produtoVetorial(p,q),produtoVetorial(q,r)),produtoVetorial(r,p));
		    return divisaoPorEscalar(soma,norma(soma));
		}

		//Checa se o ponto q está sobre o segmento pr
		static boolean estaSobreALinha(Vetor p, Vetor q, Vetor r)
		{

		    if(q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()) && q.getZ() <= Math.max(p.getZ(), r.getZ()) && q.getZ() >= Math.min(p.getZ(), r.getZ()))
		    {
		        return true;
		    }
		    return false;
		}

		//Encontra a orientação (linear, horário, anti-horário) dos 3 pontos p,q e r
		static int orientacao(Vetor p, Vetor q, Vetor r, Vetor normal)
		{
		    double val = produtoEscalar(normal,produtoVetorial(subtracaoVetorial(q,p),subtracaoVetorial(r,p)));

		    if (val == 0)
		    {
		        return 0;  // colinear
		    }
		    return (val > 0)? 2: 1; // horário or antihorario
		}

		//Retorna true se o segmento p1q1 e p2q2 se cruzam
		static boolean cruza(Vetor p1, Vetor q1, Vetor p2, Vetor q2, Vetor normal)
		{
		    //Encontra as 4 orietacoes necessarias para o caso geral e os especiais
		    int o1 = orientacao(p1,q1,p2,normal);
		    int o2 = orientacao(p1,q1,q2,normal);
		    int o3 = orientacao(p2,q2,p1,normal);
		    int o4 = orientacao(p2,q2,q1,normal);

		    //Caso geral
		    if (o1 != o2 && o3 != o4)
		    {
		        return true;
		    }

		    //Casos especiais
		    //p1,q1 e p2 são colineares e p2 está sobre p1q1
		    if (o1 == 0 && estaSobreALinha(p1, p2, q1)) return true;

		    //p1,q1 e p2 sãp colineares e q2 está sobre p1q1
		    if (o2 == 0 && estaSobreALinha(p1, q2, q1)) return true;

		    //p2,q2 e p1 são colineares e p1 está sobre p2q2
		    if (o3 == 0 && estaSobreALinha(p2, p1, q2)) return true;

		    //p2,q2 e q1 são colineares e q1 está sobre p2q2
		    if (o4 == 0 && estaSobreALinha(p2, q1, q2)) return true;

		    return false; //Não cai em nenhum dos casos acima

		}

		static boolean estaDentro(Poligono pol, Vetor p, Vetor normal)
		{
		    ArrayList<Vetor> aux = pol.getVertices();
		    int n = aux.size();

		    Vetor extremo = new Vetor(10000,10000,p.getZ());

		    //Conta as intersecoes da linha acima com os lados do poligono
		    int conta = 0, i = 0;

		    do{

		        int proximo = (i+1)%n;

		        //Checa se o segmento de p até o extremo cruza o segmento de aux[i] até aux[proximo]
		        if (cruza(aux.get(i), aux.get(proximo), p, extremo, normal))
		        {
		            //Se o ponto p é colinear com o segmento i-proximo, então verifica se ele está sobre o segmento. Se ele estiver, retorna true, do contrário, false
		            if (orientacao(aux.get(i), p, aux.get(proximo), normal) == 0)
		            {
		                return estaSobreALinha(aux.get(i), p, aux.get(proximo));
		            }
		            conta++;
		        }
		        i = proximo;
		    }while (i != 0);

		    //Retorna true se conta é ímpar, falso do contrário
		    if ((conta & 1) == 0)
		    { 
		    	return false; 
		    } 
		    else 
		    { 
		    	return true; 
		    }
		}
}
