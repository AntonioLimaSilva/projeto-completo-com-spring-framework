package br.com.luciano.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	public abstract String salvarTemporariamente(MultipartFile[] files);

	public abstract byte[] recuperarFotoLocal(String nomeFoto);

	public abstract void salvarFoto(String nomeFoto);

	public abstract byte[] recuperarFotoTemporaria(String nomeFoto);

	public abstract byte[] recuperarThumbnail(String foto);

}
