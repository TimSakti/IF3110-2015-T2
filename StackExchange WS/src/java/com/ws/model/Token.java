package com.ws.model;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Token {
  public String access_token;
  public String expire;
} 
