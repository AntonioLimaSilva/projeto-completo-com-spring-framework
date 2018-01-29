package br.com.luciano.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import br.com.luciano.brewer.dto.FotoDTO;
import br.com.luciano.brewer.storage.FotoStorage;
import br.com.luciano.brewer.storage.FotoStorageRunnable;

/**
 * 
 * @author Luciano Lima
 *
 */
@RestController
@RequestMapping("/fotos")
public class FotosController {
	
	@Autowired
	private FotoStorage fotoStorage;

	@PostMapping
	public DeferredResult<FotoDTO> foto(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();		
		new Thread(new FotoStorageRunnable(files, resultado, fotoStorage)).start();
		return resultado;
	}
	
	@GetMapping("/temp/{nome:.*}")
	public byte [] recuperarFotoTemporaria(@PathVariable("nome") String nomeFoto) {
		return fotoStorage.recuperarFotoTemporaria(nomeFoto);
	}
	
	@GetMapping(value = "/local/{nome:.*}")
	public byte[] recuperarFoto(@PathVariable("nome") String nomeFoto) {
		return fotoStorage.recuperarFotoLocal(nomeFoto);
	}
}
