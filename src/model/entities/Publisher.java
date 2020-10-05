package model.entities;

import java.io.Serializable;

public class Publisher implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer publisher_id;
	private String name;
	private String url;

	
	
	public Publisher() {
	}

	public Publisher(Integer id, String name, String url) {
		this.publisher_id = id;
		this.name = name;
		this.url = url;
		
	}

	public Integer getId() {
		return publisher_id;
	}

	public void setId(Integer id) {
		this.publisher_id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((publisher_id == null) ? 0 : publisher_id.hashCode());
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
		Publisher other = (Publisher) obj;
		if (publisher_id == null) {
			if (other.publisher_id != null)
				return false;
		} else if (!publisher_id.equals(other.publisher_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
