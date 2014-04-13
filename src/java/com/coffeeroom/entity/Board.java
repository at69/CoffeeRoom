package com.coffeeroom.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "board")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Board.findAll", query = "SELECT b FROM Board b"),
    @NamedQuery(name = "Board.findById", query = "SELECT b FROM Board b WHERE b.id = :id"),
    @NamedQuery(name = "Board.findByName", query = "SELECT b FROM Board b WHERE b.name = :name"),
    @NamedQuery(name = "Board.findByDescription", query = "SELECT b FROM Board b WHERE b.description = :description"),
    @NamedQuery(name = "Board.findByCreationDate", query = "SELECT b FROM Board b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "Board.findByAuthorId", query = "SELECT b FROM Board b WHERE b.authorId = :authorId"),
    @NamedQuery(name = "Board.findByCategoryId", query = "SELECT b FROM Board b WHERE b.categoryId = :categoryId")})
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "author_id")
    private int authorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "category_id")
    private int categoryId;

    public Board() {
    }

    public Board(Integer id) {
        this.id = id;
    }

    public Board(Integer id, String name, String description, Date creationDate, int authorId, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.authorId = authorId;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Board)) {
            return false;
        }
        Board other = (Board) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.coffeeroom.entity.Board[ id=" + id + " ]";
    }
    
}
