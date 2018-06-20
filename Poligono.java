import java.awt.*;
import java.util.*;
import java.util.List;

public class Poligono extends Objeto{
	
	ArrayList<Vetor> vertices;

	public Poligono(ArrayList<Vetor> vec, Color c)
	{
		vertices = vec;
		cor = c;
	}
	
	public ArrayList<Vetor> getVertices()
	{
		return vertices;
	}
	
	public void setVertices(ArrayList<Vetor> novosVertices)
	{
		this.vertices = novosVertices;
	}
	
	public void aplicarOffset(List<Integer> offsets) 
	{
		for(int i = 0; i < vertices.size(); i++)
		{
			this.vertices.get(i).setX(vertices.get(i).getX() + offsets.get(0));
			this.vertices.get(i).setY(vertices.get(i).getY() + offsets.get(1));
			this.vertices.get(i).setZ(vertices.get(i).getZ() + offsets.get(2));
		}
	}
	
	public Vetor calcularNormal(Vetor ponto)
	{
		Vetor aux1 = null, aux2 = null;
		
		for(int i = 0; i < vertices.size(); i++)
		{
			if (i < vertices.size()-1)
			{
				if(AlgebraLinear.orientacao(ponto, vertices.get(i), vertices.get(i+1), AlgebraLinear.getNormal(this)) == 2)
				{
					aux1 = vertices.get(i);
					aux2 = vertices.get(i+1);
				}
			}
			else if(AlgebraLinear.orientacao(ponto, vertices.get(i), vertices.get(0), AlgebraLinear.getNormal(this)) == 2)
			{
				aux1 = vertices.get(i);
				aux2 = vertices.get(0);
			}
		}
		
		Vetor auxiliar = AlgebraLinear.produtoVetorial(AlgebraLinear.subtracaoVetorial(aux1, ponto), AlgebraLinear.subtracaoVetorial(aux2, ponto));
		
		return AlgebraLinear.divisaoPorEscalar(auxiliar, AlgebraLinear.norma(auxiliar));
	}
}
