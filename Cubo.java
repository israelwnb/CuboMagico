import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Cubo extends JPanel {
	
	Vetor e;
	
	ArrayList<Poligono> poligonos;
	
	BufferedImage imagem;
	
	int left,right,top,bottom;
	
	int op,conta = 0;
	
	boolean primeiraVezIlumi = true;
	boolean primeiraVezTransf = true;
	
	List<Integer> opcoes = new ArrayList<>();
	ArrayList<double[][]> transformacoes = new ArrayList<>();
	
	
	double intensidadeLuzAmbiente;
	Color corLuzAmbiente;

	double brilho;
	
	public void init()
	{
		poligonos = new ArrayList<Poligono>();

	    //Setando ponto de visão
	    e = new Vetor(2, 3, -10);

	    //Adicionando os polígonos
	    ArrayList<Vetor> v1 = new ArrayList<Vetor>() {{
	    	add(new Vetor(-3,3,5));
	    	add(new Vetor(3,3,5));
	    	add(new Vetor(3,-3,5));
	    	add(new Vetor(-3,-3,5));
	    }};
	    Poligono p1 = new Poligono(v1, new Color(255, 0, 0)); // FRENTE
	    ArrayList<Vetor> v2 = new ArrayList<Vetor>() {{
	    	add(new Vetor(-3,3,5));
	    	add(new Vetor(-3,3,11));
	    	add(new Vetor(-3,-3,11));
	    	add(new Vetor(-3,-3,5));
	    }};
	    Poligono p2 = new Poligono(v2, new Color(0, 255, 0)); //ESQUERDA
	    ArrayList<Vetor> v3 = new ArrayList<Vetor>() {{
	    	add(new Vetor(3,3,5));
	    	add(new Vetor(3,3,11));
	    	add(new Vetor(3,-3,11));
	    	add(new Vetor(3,-3,5));
	    }};
	    Poligono p3 = new Poligono(v3, new Color(0, 0, 255)); //DIREITA
	    ArrayList<Vetor> v4 = new ArrayList<Vetor>() {{
	    	add(new Vetor(-3,3,11));
	    	add(new Vetor(3,3,11));
	    	add(new Vetor(3,3,5));
	    	add(new Vetor(-3,3,5));
	    }};
	    Poligono p4 = new Poligono(v4, new Color(255, 255, 255)); //CIMA
	    ArrayList<Vetor> v5 = new ArrayList<Vetor>() {{
	    	add(new Vetor(-3,-3,11));
	    	add(new Vetor(3,-3,11));
	    	add(new Vetor(3,-3,5));
	    	add(new Vetor(-3,-3,5));
	    }};
	    Poligono p5 = new Poligono(v5, new Color(255, 255, 0)); //BAIXO
	    ArrayList<Vetor> v6 = new ArrayList<Vetor>() {{
	    	add(new Vetor(-3,3,11));
	    	add(new Vetor(3,3,11));
	    	add(new Vetor(3,-3,11));
	    	add(new Vetor(-3,-3,11));
	    }};
	    Poligono p6 = new Poligono(v6, new Color(255, 165, 0)); //FUNDO
	    poligonos.add(p1);
	    poligonos.add(p2);
	    poligonos.add(p3);
	    poligonos.add(p4);
	    poligonos.add(p5);
	    poligonos.add(p6);


	    //Ajustando o tamanho da imagem
	    imagem = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

	    //Ajustando os limites da imagem
	    left = -5;
	    right = 5;
	    top = 5;
	    bottom = -5;
	    
	    //Configurando brilho
	    brilho = 10;
	  
	    //Configurando ambiente
	    intensidadeLuzAmbiente = 0.3;
	    corLuzAmbiente =  new Color(40, 100, 150);
	    
	}
	
	public void paint(Graphics g)
	{
		init();
		Image img = rayTrace();
		g.drawImage(img, 0, 0, this);
	}
	
	public Vetor[] transformar(Vetor u, Vetor v)
	{
		Vetor auxU = u;
		Vetor auxV = v;
		Vetor[] res;
		if (primeiraVezTransf)
		{
			int posicao;
			String aux;
			System.out.println("\nEscolha quaisquer transformações quiser na imagem separando as opções escolhidas por um espaço:");
			System.out.println("1 - Cisalhamento");
			System.out.println("2 - Redimensionamento");
			System.out.println("3 - Rotação");
			System.out.println("4 - Reflexão");
			System.out.println("5 - Translação");
			Scanner s = new Scanner(System.in);
			String entrada = s.nextLine();
			if(!entrada.isEmpty())
			{
				while((posicao = entrada.indexOf(' ')) != -1)
                {
					aux = entrada.substring(0, posicao);
					opcoes.add(Integer.parseInt(aux));
					entrada = entrada.substring(posicao + 1, entrada.length());
                }
				opcoes.add(Integer.parseInt(entrada));
			}
			if(opcoes.isEmpty())
			{
				System.out.println("Você não escolheu nenhuma transformação");
			}
			else
			{
				int angulo1 = 0;
				int angulo2 = 0;
				char eixo = ' '; 
				char sentido = ' ';
				double escalaX = 0.0;
				double escalaY = 0.0;
				double escalaZ = 0.0;
				for(int i = 0; i < opcoes.size(); i++)
				{
					if(opcoes.get(i) == 1)
					{
						System.out.println("Cisalhamento");
						System.out.println("Insira os ângulos do cisalhamento e o eixo de referência. Exemplo: 45 30 x");
						s = new Scanner(System.in);
						entrada = s.nextLine();
						if(!entrada.isEmpty())
						{
							int controle = 0;
							while((posicao = entrada.indexOf(' ')) != -1)
			                {
								aux = entrada.substring(0, posicao);
								if (controle == 0)
								{
									angulo1 = (Integer.parseInt(aux));
									controle++;
								}
								else
								{
									angulo2 = (Integer.parseInt(aux));
									controle--;
								}
								entrada = entrada.substring(posicao + 1, entrada.length());
			                }
							eixo = entrada.charAt(0);
						}
						
						if(eixo == 'x')
						{
							double[][] transformacao = new double[][] {{1.0, angulo1/45.0, angulo2/45.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else if(eixo == 'y') 
						{
							double[][] transformacao = new double[][] {{angulo1/45.0, 1.0, angulo2/45.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else if(eixo == 'z') 
						{
							double[][] transformacao = new double[][] {{angulo1/45.0, angulo2/45.0, 1.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else
						{
							System.out.println("Eixo inválido");
						}
					}
					else if(opcoes.get(i) == 2)
					{
						System.out.println("Redimensionamento");
						System.out.println("Insira o fator de escala a ser aplicado a cada um dos eixos. Exemplo: 0.5 1.5");
						s = new Scanner(System.in);
						entrada = s.nextLine();
						if(!entrada.isEmpty())
						{
							int controle = 0;
							while((posicao = entrada.indexOf(' ')) != -1)
			                {
								aux = entrada.substring(0, posicao);
								if (controle == 0)
								{
									escalaX = (Double.parseDouble(aux));
									controle++;
								}
								else
								{
									escalaY = (Double.parseDouble(aux));
									controle--;
								}
								entrada = entrada.substring(posicao + 1, entrada.length());
			                }
							escalaZ = Double.parseDouble(entrada);
						}
						
						double[][] transformacao = new double[][] {{escalaX , 0.0, 0.0}, {0.0, escalaY, 0.0}, {0.0, 0.0, escalaZ}};
						transformacoes.add(transformacao);
					}
					else if(opcoes.get(i) == 3)
					{
						System.out.println("Rotação");
						System.out.println("Insira o ângulo a ser aplicado na transformação da imagem e o eixo da rotação (x, y ou z). Exemplo: 45 z");
						s = new Scanner(System.in);
						entrada = s.nextLine();
						if(!entrada.isEmpty())
						{
							while((posicao = entrada.indexOf(' ')) != -1)
			                {
								aux = entrada.substring(0, posicao);
								angulo1 = (Integer.parseInt(aux));
								entrada = entrada.substring(posicao + 1, entrada.length());
			                }
							eixo = entrada.charAt(0);
						}
						
						if(eixo == 'x')
						{
							double[][] transformacao = new double[][] {{1.0, 0.0, 0.0}, {0.0, Math.cos(Math.toRadians(angulo1)), -1*Math.sin(Math.toRadians(angulo1))}, {0.0, Math.sin(Math.toRadians(angulo1)), Math.cos(Math.toRadians(angulo1))}};
							transformacoes.add(transformacao);
						}
						else if(eixo == 'y') 
						{
							double[][] transformacao = new double[][] {{Math.cos(Math.toRadians(angulo1)), 0.0, Math.sin(Math.toRadians(angulo1))}, {0.0, 1.0, 0.0}, {-1*Math.sin(Math.toRadians(angulo1)), 0.0, Math.cos(Math.toRadians(angulo1))}};
							transformacoes.add(transformacao);
						}
						else if (eixo == 'z')
						{
							double[][] transformacao = new double[][] {{Math.cos(Math.toRadians(angulo1)), -1*Math.sin(Math.toRadians(angulo1)), 0.0}, {Math.sin(Math.toRadians(angulo1)), Math.cos(Math.toRadians(angulo1)), 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else
						{
							System.out.println("Eixo inválido");
						}
					}
					else if(opcoes.get(i) == 4)
					{
						System.out.println("Reflexão");
                        System.out.println("Insira o eixo a servir de espelho para a reflexão (x//y//z. Exemplo: y");
                        s = new Scanner(System.in);
                        eixo = s.next().charAt(0);
                        
                        if(eixo == 'x')
						{
							double[][] transformacao = new double[][] {{1.0 , 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, -1.0}};
							transformacoes.add(transformacao);
						}
						else if(eixo == 'y') 
						{
							double[][] transformacao = new double[][] {{1.0 , 0.0, 0.0}, {0.0, -1.0, 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else if(eixo == 'z')
						{
							double[][] transformacao = new double[][] {{-1.0 , 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
							transformacoes.add(transformacao);
						}
						else
						{
							System.out.println("Eixo inválido");
						}
					}
					else if(opcoes.get(i) == 5)
					{
						System.out.println("Translação");
						System.out.println("Insira o offset a ser aplicado a cada eixo (x, y e z, respectivamente). Exemplo: 1 -2 0");
						List<Integer> offsets = new ArrayList<>();
						s = new Scanner(System.in);
						entrada = s.nextLine();
						if(!entrada.isEmpty())
						{
							while((posicao = entrada.indexOf(' ')) != -1)
			                {
								aux = entrada.substring(0, posicao);
								offsets.add(Integer.parseInt(aux));
								entrada = entrada.substring(posicao + 1, entrada.length());
			                }
							offsets.add(Integer.parseInt(entrada));
						}
						if(offsets.size() < 3)
						{
							System.out.println("Entradas insuficientes");
						}
						else
						{
							for(int k = 0; k < poligonos.size(); k++)
							{
								poligonos.get(i).aplicarOffset(offsets);
							}
						}
					}
					else 
					{
						System.out.println(opcoes.get(i).toString() + " é uma opção inválida");
					}
				}
			}
			//s.close();
			primeiraVezTransf = false;
		}
		for(int n = 0; n < transformacoes.size(); n++)
		{
			auxU = AlgebraLinear.transformacaoLinear(transformacoes.get(n), auxU);
			auxV = AlgebraLinear.transformacaoLinear(transformacoes.get(n), auxV);
		}
		
		res = new Vetor[] {auxU, auxV};
		return res;
	}

	public Image rayTrace() 
	{
		Vetor[] arrayAux;
		
		Vetor w = AlgebraLinear.divisaoPorEscalar(e,AlgebraLinear.norma(e));

	    Vetor vetorT = AlgebraLinear.chutarT(w);
	    Vetor u = AlgebraLinear.divisaoPorEscalar(AlgebraLinear.produtoVetorial(vetorT,w),AlgebraLinear.norma(AlgebraLinear.produtoVetorial(vetorT,w)));

	    Vetor v = AlgebraLinear.produtoVetorial(w,u);
	    
	    arrayAux = transformar(u,v);
	    u = arrayAux[0];
	    v = arrayAux[1];
	    
	    for (int i = 0; i < imagem.getWidth(); i++)
	    {
	        for (int j = 0; j < imagem.getHeight(); j++)
	        {
	            double uzao = left + (right-left)*(i+0.5)/imagem.getWidth();
	            double vzao = bottom + (top-bottom)*(j+0.5)/imagem.getHeight();
	            Vetor o = AlgebraLinear.somaVetorial(AlgebraLinear.somaVetorial(e,AlgebraLinear.multiplicacaoPorEscalar(u,uzao)),AlgebraLinear.multiplicacaoPorEscalar(v,vzao));

	            Vetor d = AlgebraLinear.multiplicacaoPorEscalar(w, -1);

	            double t;

	            //PARTE DOS POLIGONOS

	            Vetor n = new Vetor(0,0,0);
	            Vetor p = new Vetor(0,0,0);

	            ArrayList<Double> resPoligonos = new ArrayList<Double>();

	            for (int y = 0; y < poligonos.size(); y++)
	            {
	                n = AlgebraLinear.getNormal(poligonos.get(y));

	                double numerador = AlgebraLinear.produtoEscalar(AlgebraLinear.subtracaoVetorial(poligonos.get(y).getVertices().get(0),o),n);
	                double denominador = AlgebraLinear.produtoEscalar(d,n);

	                if (denominador != 0)
	                {
	                    t = numerador/denominador;
	                    p = AlgebraLinear.somaVetorial(o, AlgebraLinear.multiplicacaoPorEscalar(d,t));
	                    if (AlgebraLinear.estaDentro(poligonos.get(y), p,n))
	                    {
	                    	System.out.println("Dentro");
	                        resPoligonos.add(t);
	                    }
	                    else
	                    {
	                    	System.out.println("Fora");
	                        //cout << errou
	                        t = Double.POSITIVE_INFINITY;
	                        resPoligonos.add(t);
	                    }

	                }
	                else
	                {
	                    t = Double.POSITIVE_INFINITY;
	                    resPoligonos.add(t);
	                }
	            }

	            int menorPoligonos = (int) Double.POSITIVE_INFINITY;
	            for (int k = 0; k < resPoligonos.size(); k++)
	            {
	                if (menorPoligonos == (int)Double.POSITIVE_INFINITY && resPoligonos.get(k) != Double.POSITIVE_INFINITY){
	                    menorPoligonos = k;
	                }
	                else if (menorPoligonos != (int)Double.POSITIVE_INFINITY && resPoligonos.get(k) < resPoligonos.get(menorPoligonos)){
	                    menorPoligonos = k;
	                }
	            }
	            
	            //ILUMINAÇÃO
	            
	            //TODO Ajeitar essa porra
	            
	            if (menorEsferas == (int)Double.POSITIVE_INFINITY && menorPoligonos == (int)Double.POSITIVE_INFINITY)
	            {
	                imagem.setRGB(i, j, Color.black.getRGB());
	            }
	            if (menorEsferas == (int)Double.POSITIVE_INFINITY && menorPoligonos != (int)Double.POSITIVE_INFINITY)
	            {
	            	Poligono objeto = poligonos.get(menorPoligonos);
	            	Vetor ponto = AlgebraLinear.somaVetorial(o, AlgebraLinear.multiplicacaoPorEscalar(d, resPoligonos.get(menorPoligonos)));
	                imagem.setRGB(i,j,iluminar(objeto,ponto,objeto.calcularNormal(ponto)));
	            }
	            if (menorEsferas != (int)Double.POSITIVE_INFINITY && menorPoligonos == (int)Double.POSITIVE_INFINITY)
	            {
	            	Esfera objeto = esferas.get(menorEsferas);
	            	Vetor ponto = AlgebraLinear.somaVetorial(o, AlgebraLinear.multiplicacaoPorEscalar(d, resEsferas.get(menorEsferas)));
	                imagem.setRGB(i,j,iluminar(objeto,ponto,objeto.calcularNormal(ponto)));
	            }
	            if (menorEsferas != (int)Double.POSITIVE_INFINITY && menorPoligonos != (int)Double.POSITIVE_INFINITY)
	            {
	                if (menorEsferas != (int)Double.POSITIVE_INFINITY && menorPoligonos != (int)Double.POSITIVE_INFINITY && resEsferas.get(menorEsferas) <= resPoligonos.get(menorPoligonos))
	                {
	                	Esfera objeto = esferas.get(menorEsferas);
	                	Vetor ponto = AlgebraLinear.somaVetorial(o, AlgebraLinear.multiplicacaoPorEscalar(d, resEsferas.get(menorEsferas)));
	                    imagem.setRGB(i,j,iluminar(objeto,ponto,objeto.calcularNormal(ponto)));
	                }
	                else if(menorEsferas != (int)Double.POSITIVE_INFINITY && menorPoligonos != (int)Double.POSITIVE_INFINITY ){
	                	Poligono objeto = poligonos.get(menorPoligonos);
		            	Vetor ponto = AlgebraLinear.somaVetorial(o, AlgebraLinear.multiplicacaoPorEscalar(d, resPoligonos.get(menorPoligonos)));
	                    imagem.setRGB(i,j,iluminar(objeto,ponto,objeto.calcularNormal(ponto)));
	                }
	            }
	        }
	    }
	    if (conta == 0) {
	    	conta++;
	    }
	    else {
	    	conta = 0;
	    	opcoes = new ArrayList<>();
	    	transformacoes = new ArrayList<>();
	    	primeiraVezIlumi = true; 
	    	primeiraVezTransf = true;
	    }
	    return imagem;
	    
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Cubo rayTracing = new Cubo();
		frame.getContentPane().add(rayTracing);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setVisible(true);
		while(true)
		{
			rayTracing = new Cubo();
			frame.getContentPane().repaint();
		}
		
	}
}
