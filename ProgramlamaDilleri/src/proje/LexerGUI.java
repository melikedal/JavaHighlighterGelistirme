package proje;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class GUI extends JFrame implements ActionListener {
	JPanel jpl;
	JButton tokenizeButon;
	JTextField jtf;
	
	 JTextPane girdiAlani;
	 JTextArea ciktiAlani;
	 public Timer renklendirZamanlayici;
	
	
	GUI(String baslik)
	{
		super(baslik);
		this.setSize(800,600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		
		
		
		
		girdiAlani=new JTextPane();
		JScrollPane girisEkraniKaydirma=new JScrollPane(girdiAlani);
		
		girisEkraniKaydirma.setBounds(20, 20, 740, 200);
		
		 Font girisBaslikFont = new Font("Courier New", Font.BOLD, 14);
		 
		TitledBorder baslikTasarim = BorderFactory.createTitledBorder("Kod Giriniz");
		baslikTasarim.setTitleJustification(TitledBorder.CENTER);  
		baslikTasarim.setTitleFont(girisBaslikFont);                   
		baslikTasarim.setTitleColor(Color.black);  
		
	        girdiAlani.setBorder(baslikTasarim);

        this.add(girisEkraniKaydirma);
        
       StyledDocument belge = girdiAlani.getStyledDocument();
        StyleContext stil = StyleContext.getDefaultStyleContext();
        
       
       
        Style anahtarKelimeStili = girdiAlani.addStyle("KEYWORD", null);
        StyleConstants.setForeground(anahtarKelimeStili, Color.BLUE);

        Style identifierStil = girdiAlani.addStyle("IDENTIFIER", null);
        StyleConstants.setForeground(identifierStil, Color.BLACK);

        Style sayiStil = girdiAlani.addStyle("NUMBER", null);
        StyleConstants.setForeground(sayiStil, new Color(128, 0, 128)); // Mor

        Style operatorStil = girdiAlani.addStyle("OPERATOR", null);
        StyleConstants.setForeground(operatorStil, Color.RED);

        Style punctuationStil = girdiAlani.addStyle("PUNCTUATION", null);
        StyleConstants.setForeground(punctuationStil, Color.GRAY);

        Style unknownStil = girdiAlani.addStyle("UNKNOWN", null);
        StyleConstants.setForeground(unknownStil, Color.ORANGE);
        
        Style whitespaceStil = girdiAlani.addStyle("WHITESPACE", null);
        StyleConstants.setForeground(whitespaceStil, Color.LIGHT_GRAY);
        
        Style yorumSatiriStil = girdiAlani.addStyle("COMMENT", null);
        StyleConstants.setForeground(yorumSatiriStil, Color.GREEN.darker());

        Style stringStyle = girdiAlani.addStyle("STRING", null);
        StyleConstants.setForeground(stringStyle, new Color(163, 21, 21));
        
        
      
        
        Style defaultStyle = stil.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        
        
        ciktiAlani=new JTextArea();
        ciktiAlani.setEditable(false);
        JScrollPane cikisEkraniKaydirma = new JScrollPane(ciktiAlani);
        cikisEkraniKaydirma.setBounds(20, 280, 740, 250);
       
      
       
       Font cikisBaslikFont = new Font("Courier New", Font.BOLD, 14);
       
       TitledBorder baslikTasarim2 = BorderFactory.createTitledBorder("Token Çıktısı");
		baslikTasarim2.setTitleJustification(TitledBorder.CENTER);  
		baslikTasarim2.setTitleFont(cikisBaslikFont);                    
		baslikTasarim2.setTitleColor(Color.black);                   
	        
	        ciktiAlani.setBorder(baslikTasarim2);

        this.add(cikisEkraniKaydirma);
       
        
       
        tokenizeButon=new JButton("Tokenize Et!");
        tokenizeButon.setBounds(320, 230, 150,30);
        tokenizeButon.setBackground(Color.gray);
        tokenizeButon.setForeground(Color.WHITE);
        tokenizeButon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tokenizeButon.setFocusPainted(false);
        tokenizeButon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        tokenizeButon.setOpaque(true);
        tokenizeButon.addActionListener(this);
        this.add(tokenizeButon);
        
        
        renklendirZamanlayici = new Timer(200, e -> kodRenklendir());
        renklendirZamanlayici.setRepeats(false); 

       
        girdiAlani.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                renklendirZamanlayici.restart();
            }

            public void removeUpdate(DocumentEvent e) {
                renklendirZamanlayici.restart();
            }

            public void changedUpdate(DocumentEvent e) {
                renklendirZamanlayici.restart();
            }
        });
        
        
        
        
		this.setVisible(true);
		
		
		
	}
	
	public void kodRenklendir() {
	    SwingUtilities.invokeLater(() -> {
	        StyledDocument belge = girdiAlani.getStyledDocument();
	        String metin2 = girdiAlani.getText();

	       
	        belge.setCharacterAttributes(0, metin2.length(), girdiAlani.getStyle("default"), true);

	        List<Token> tokens = Token.tokenize(metin2);
	        int pos = 0;
	        for (Token token : tokens) {
	            int k = token.metin.length();
	            Style stil2 = girdiAlani.getStyle(token.tip.toString());
	            if (stil2 != null) {
	                belge.setCharacterAttributes(pos, k, stil2, true);
	            }

	            pos += k;
	        }
	    });
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String kod=girdiAlani.getText();
		List <Token> tokens = Token.tokenize(kod);

		
		StringBuilder builder=new StringBuilder();
		for(Token token:tokens)
		{
			builder.append(token).append("\n");
		}
		
		builder.append("\n Sözdizimi Analizi:\n");
		builder.append(SözDizimiAnalizi.sözDizimi(tokens));
		
   
		
		ciktiAlani.setText(builder.toString());
		
	}
	
	   

public enum TokenTipi{
	KEYWORD,IDENTIFIER,NUMBER,OPERATOR,PUNCTUATION,WHITESPACE,UNKNOWN,COMMENT,STRING, CHAR_DEGER
}


public static class Token{
	public final TokenTipi tip;
	public final String metin;
	
	public Token(TokenTipi tip,String metin)
	{
		this.tip=tip;
		this.metin=metin;
		
		
	}
	public String toString() 
	{
		return tip+":"+ metin;
	
     }
	
	
	public static List<Token> tokenize(String girdi){
		String[] KEYWORDS= {"abstract","boolean", "break","case", "catch", "const", "continue","int", "float", "double", "char", "boolean",
        "if",  "interface", "implements", "import", "instanceof","else", "while", "for", "return", "void", "class", "public", "private", "protected", "static", "super", "switch","String", "this", "throw", "throws", "new"};
		char[] OPERATORS= {'+', '-', '*', '/', '=','<','>'};
		char[] PUNCTUATIONS= {'(', ')', ';', '{', '}','.'};
		char[] WHITESPACE= {};
		
		List<Token> tokens=new ArrayList<>();
		int i=0;
	
		
	while(i< girdi.length())
		{
			char karakter=girdi.charAt(i);
			
			
			if(Character.isWhitespace(karakter))
			{
				int start=i;
				while(i<girdi.length()&& Character.isWhitespace(girdi.charAt(i)))
					i++;
				tokens.add(new Token(TokenTipi.WHITESPACE,girdi.substring(start, i)));
			}
			
			else if (karakter == '/' && i + 1 < girdi.length() && girdi.charAt(i + 1) == '/') {
			    int start = i;
			    i += 2; // "//" geç
			    while (i < girdi.length() && girdi.charAt(i) != '\n') i++;
			    tokens.add(new Token(TokenTipi.COMMENT, girdi.substring(start, i)));
			}

			else if(Character.isDigit(karakter)) 
			{
				int start=i;
				while(i<girdi.length()&& Character.isDigit(girdi.charAt(i)))
					i++;
				tokens.add(new Token(TokenTipi.NUMBER,girdi.substring(start, i)));
				
			}
			else if(Character.isLetter(karakter) || karakter == '_')
			{
				int start = i;
	            while (i < girdi.length() && (Character.isLetterOrDigit(girdi.charAt(i)) || girdi.charAt(i) == '_')) 
	            	i++;
	            String kelime = girdi.substring(start, i);
	            boolean isKeyword = Arrays.asList(KEYWORDS).contains(kelime);
	            tokens.add(new Token(isKeyword ? TokenTipi.KEYWORD : TokenTipi.IDENTIFIER, kelime));
	            
			}
			else if (new String(OPERATORS).indexOf(karakter) >= 0)
			{
	            tokens.add(new Token(TokenTipi.OPERATOR, String.valueOf(karakter)));
	            i++;
		    }
			else if(karakter == '"') { // STRING LITERAL
	            int start = i;
	            i++; // ilk tırnak geç
	            boolean kapandi = false;
	            while (i < girdi.length()) {
	                if (girdi.charAt(i) == '\\') { // escape karakter atla
	                    i += 2;
	                    continue;
	                }
	                if (girdi.charAt(i) == '"') {
	                    kapandi = true;
	                    i++;
	                    break;
	                }
	                i++;
	            }
	            if(!kapandi) {
	             
	            }
	            tokens.add(new Token(TokenTipi.STRING, girdi.substring(start, i)));
	        }
	        else if(karakter == '\'') { 
	            int start = i;
	            i++; 
	            if(i < girdi.length()) {
	                if(girdi.charAt(i) == '\\') {
	                    i += 2; 
	                } else {
	                    i++;
	                }
	            }
	            if(i < girdi.length() && girdi.charAt(i) == '\'') {
	                i++;
	            }
	            tokens.add(new Token(TokenTipi.CHAR_DEGER, girdi.substring(start, i)));}
			
			
			else if (new String(PUNCTUATIONS).indexOf(karakter) >= 0) 
			{
	            tokens.add(new Token(TokenTipi.PUNCTUATION, String.valueOf(karakter)));
	            i++;
	        } else 
	        {
	            tokens.add(new Token(TokenTipi.UNKNOWN, String.valueOf(karakter)));
	            i++;
	        }
			
	}
		return tokens;


	}


}


  
public static class SözDizimiAnalizi {
    public static String sözDizimi(List<Token> tokens) {
        StringBuilder sonuc = new StringBuilder();
        int i = 0;

        List<String> veriTurleri = Arrays.asList("int", "float", "double", "char", "boolean");

        while (i < tokens.size()) {
            i = atlaBosluklari(tokens, i);

            if (i >= tokens.size()) break;

            Token token = tokens.get(i);

            if (token.tip == TokenTipi.KEYWORD && veriTurleri.contains(token.metin)) {
                String veriTuru = token.metin;  
                i++;
                i = atlaBosluklari(tokens, i);

                if (i < tokens.size() && tokens.get(i).tip == TokenTipi.IDENTIFIER) {
                    i++;
                    i = atlaBosluklari(tokens, i);

                    if (i < tokens.size() && tokens.get(i).tip == TokenTipi.OPERATOR && tokens.get(i).metin.equals("=")) {
                        i++;
                        i = atlaBosluklari(tokens, i);

                        if (i < tokens.size()) {
                            Token degerToken = tokens.get(i);

                            boolean dogruDeger = false;

                           
                            if ("char".equals(veriTuru)) {
                                if (degerToken.tip == TokenTipi.CHAR_DEGER) {  
                                    dogruDeger = true;
                                }
                            } else if ("boolean".equals(veriTuru)) {
                                if (degerToken.tip == TokenTipi.KEYWORD &&
                                    (degerToken.metin.equals("true") || degerToken.metin.equals("false"))) {
                                    dogruDeger = true;
                                }
                            } else if ("int".equals(veriTuru) || "float".equals(veriTuru) || "double".equals(veriTuru)) {
                                if (degerToken.tip == TokenTipi.NUMBER) {
                                    dogruDeger = true;
                                }
                            }

                            if (dogruDeger) {
                                i++;
                                i = atlaBosluklari(tokens, i);

                                if (i < tokens.size() && tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals(";")) {
                                    sonuc.append(" Geçerli değişken tanımı\n");
                                    i++;
                                    continue;
                                }
                            }
                        }
                    }
                }

                sonuc.append("Hatalı değişken tanımı\n");
                break;

            } 
            else if (token.tip == TokenTipi.KEYWORD && token.metin.equals("if")) {
                i++;
                i = atlaBosluklari(tokens, i);

                
                if (i < tokens.size() && tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals("(")) {
                    i++;
                    i = atlaBosluklari(tokens, i);

                    
                    if (i + 2 < tokens.size()
                        && tokens.get(i).tip == TokenTipi.IDENTIFIER
                        && tokens.get(i + 1).tip == TokenTipi.OPERATOR
                        && tokens.get(i + 2).tip == TokenTipi.NUMBER) {

                        i += 3;
                        i = atlaBosluklari(tokens, i);

                        
                        if (i < tokens.size() && tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals(")")) {
                            i++;
                            i = atlaBosluklari(tokens, i);

                            
                            if (i < tokens.size() && tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals("{")) {
                                i++;
                                i = atlaBosluklari(tokens, i);

                                while (i < tokens.size() && !(tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals("}"))) {
                                    i++;
                                }
                                if (i < tokens.size()) {  
                                    i++;
                                    i = atlaBosluklari(tokens, i);

                                   
                                    if (i < tokens.size() && tokens.get(i).tip == TokenTipi.KEYWORD && tokens.get(i).metin.equals("else")) {
                                        i++;
                                        i = atlaBosluklari(tokens, i);

                                        if (i < tokens.size() && tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals("{")) {
                                            i++;
                                            i = atlaBosluklari(tokens, i);

                                            while (i < tokens.size() && !(tokens.get(i).tip == TokenTipi.PUNCTUATION && tokens.get(i).metin.equals("}"))) {
                                                i++;
                                            }
                                            if (i < tokens.size()) {
                                                i++;
                                                sonuc.append(" Geçerli if-else yapısı bulundu\n");
                                                continue;
                                            }
                                        }
                                    } else {
                                        sonuc.append(" Geçerli if yapısı bulundu (else yok)\n");
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }

                sonuc.append(" Hatalı if-else yapısı\n");
                break;
            } 
           
            else {
                sonuc.append("Tanınmayan başlangıç").append(token.metin).append("\n");
                break;
            }
        }

        return sonuc.toString();
    }

    
    public static int atlaBosluklari(List<Token> tokens, int i) {
        while (i < tokens.size() && tokens.get(i).tip == TokenTipi.WHITESPACE) {
            i++;
        }
        return i;
    }
}


	
}

public class LexerGUI {

	public static void main(String[] args) {
		
     new GUI("GUI ile Tokenize");
	}

}

