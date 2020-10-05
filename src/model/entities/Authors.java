package model.entities;

import java.io.Serializable;

public class Authors implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String fname;
	
	
	public Authors() {
	}

	public Authors(Integer id, String name, String fName) {
		this.id = id;
		this.nome = name;
		this.fname = fName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return nome;
	}

	public void setName(String name) {
		this.nome = name;
	}
	public String getfName() {
		return fname;
	}

	public void setfName(String fName) {
		this.fname = fName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authors other = (Authors) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

	

	
}
