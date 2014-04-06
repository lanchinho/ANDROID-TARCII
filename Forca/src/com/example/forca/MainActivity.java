package com.example.forca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private List<String> words;
	private String selectedWord;
	private char[] resultWord;
	private String letrasUsadas;
	private int numLetrasRestantes;
	private int numErros;
	// imagens referentes as partes do corpo
	private ImageView[] bodyParts;
	private int numParts = 6; // qtd de partes do corpo
	private int currPart; // incrementado a cada erro

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadNames();

		// instanciando o vetor e determinando em que ordem carregar as partes
		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView) findViewById(R.id.cabeca);
		bodyParts[1] = (ImageView) findViewById(R.id.corpo);
		bodyParts[2] = (ImageView) findViewById(R.id.perna1);
		bodyParts[3] = (ImageView) findViewById(R.id.perna2);
		bodyParts[4] = (ImageView) findViewById(R.id.braco1);
		bodyParts[5] = (ImageView) findViewById(R.id.braco2);

		Button botao = (Button) findViewById(R.id.button1);
		botao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (numLetrasRestantes <= 0 || numErros >= 6) {
					escondePartes();
					newGame();
				} else {
					String texto = ((EditText) findViewById(R.id.tentativa))
							.getText().toString();
					if (texto.length() <= 0)
						return;
					char letra = texto.toUpperCase().charAt(0);
					testaLetra(letra);
				}
			}
		});

		escondePartes();
		newGame();
	}

	private void escondePartes() {
		numParts = 0; // colocando numero de partes do corpo como zero a cada
						// novo jogo

		// Hurr ... Durr escondendo as partes do corpo (percorrer o vetor)
		for (int corpo = 0; corpo < numParts; corpo++) {
			bodyParts[corpo].setVisibility(View.INVISIBLE);
		}
	}

	private void newGame() {
		Random random = new Random();
		selectedWord = words.get(random.nextInt(words.size()));
		// Toast.makeText(this,selectedWord, Toast.LENGTH_LONG).show();

		TextView text = (TextView) findViewById(R.id.tentativa);
		text.setText("");
		
		//escondePartes();
		letrasUsadas = "";
		numLetrasRestantes = selectedWord.length();
		resultWord = selectedWord.toCharArray();
		for (int c = 0; c < selectedWord.length(); c++) {
			resultWord[c] = '_';
		}

		Button botao = (Button) findViewById(R.id.button1);
		botao.setText("TESTAR LETRA");
		setaText();
	}

	protected void testaLetra(char letra) {
		for (int c = 0; c < letrasUsadas.length(); c++) {
			if (letrasUsadas.charAt(c) == letra)
				return;
		}
		letrasUsadas += letra;
		int tempNumLetrasRestantes = numLetrasRestantes;
		for (int c = 0; c < selectedWord.length(); c++) {
			if (selectedWord.charAt(c) == letra && resultWord[c] == '_') {
				resultWord[c] = letra;
				numLetrasRestantes--;
			}
		}
		if (numLetrasRestantes == tempNumLetrasRestantes) {
			numErros++;
		}

		setaText();

		TextView text = (TextView) findViewById(R.id.tentativa);
		Button botao = (Button) findViewById(R.id.button1);
		if (numLetrasRestantes <= 0) {
			text.setText("Ganhou");
			botao.setText("NOVO JOGO");
		}
		
		//Mostra na tela uma parte do corpo a cada erro
		while(currPart < numErros){
			bodyParts[currPart].setVisibility(View.VISIBLE);
			currPart++;
			
			if (numErros == 6) {
			   //encera o jogo
				text.setText("perdeu");
				botao.setText("NOVO JOGO");
				break;
			}	
		}
	}

	public void setaText() {
		TextView text = (TextView) findViewById(R.id.resposta);
		text.setText(String.valueOf(resultWord));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void loadNames() {
		words = new ArrayList<String>();
		try {
			InputStream stream = this.getAssets().open("forca.txt");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			String line;

			while ((line = br.readLine()) != null) {
				words.add(line);
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}
	}
}
