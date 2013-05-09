package br.com.entities;

public class Cartao {
	
	private int id;
	private Float saldo;
	
	public Cartao() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "Cartao [id=" + id + ", saldo=" + saldo + "]";
	}
	

}
