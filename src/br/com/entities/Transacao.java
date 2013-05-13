package br.com.entities;

public class Transacao {
	
	private int id;
	private String data;
	private float valor;
	private int tipo;
	private String debitoCredito;
	private Cartao cartao;
	
	public Transacao() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Transacao [id=" + id + ", data=" + data + ", valor=" + valor
				+ ", tipo=" + tipo + ", debitoCredito=" + debitoCredito 
				+ ", cartao=" + cartao.getId() + "]";
	}

	public String getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(String debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}
	
	public String getTipoString() {
		String result = "";
		if (tipo == 1)
			result = "Recarga";
		else if (tipo == 2)
			result = "CPTM/Metro";
		else if (tipo == 3)
			result = "Outros";
		return result;
	}
	
	public String getDebitoCreditoString() {
		String result = "";
		if (debitoCredito.equals("C")) 
			result = "Crédito";
		else
			result = "Débito";
		return result;
	}
	
	public String toListString() {
		String result = "";
		result = getDebitoCreditoString() + " " + getTipoString() + " " + valor;
		return result;
	}
	

}
