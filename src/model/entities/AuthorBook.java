package model.entities;

import java.io.Serializable;

public class AuthorBook implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer seq_no;
	private Book isbn;
	private Authors author_id;

	public AuthorBook() {
	}

	public AuthorBook(Integer id, Book isbn, Authors a_id) {
		this.seq_no = id;
		this.isbn = isbn;
		this.author_id = a_id;
	}
	
	public Integer getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(Integer seq_no) {
		this.seq_no = seq_no;
	}

	public Book getIsbn() {
		return isbn;
	}

	public void setIsbn(Book isbn) {
		this.isbn = isbn;
	}

	public Authors getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(Authors author_id) {
		this.author_id = author_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seq_no == null) ? 0 : seq_no.hashCode());
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
		AuthorBook other = (AuthorBook) obj;
		if (seq_no == null) {
			if (other.seq_no != null)
				return false;
		} else if (!seq_no.equals(other.seq_no))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(seq_no);
	}
}
