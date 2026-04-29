package com.internship.tool.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "record")
public class RecordData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputText;
    private String result;

    public RecordData() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInputText() { return inputText; }
    public void setInputText(String inputText) { this.inputText = inputText; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}