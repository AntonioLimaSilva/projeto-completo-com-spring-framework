package br.com.luciano.brewer.storage.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import br.com.luciano.brewer.service.exception.NegocioException;
import br.com.luciano.brewer.storage.FotoStorage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class FotoStorageLocal implements FotoStorage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal() {
		this(FileSystems.getDefault().getPath(System.getProperty("user.home"), "fotostoragelocal"));
	}
	
	public FotoStorageLocal(Path local) {
		this.local = local;
		criarPastas();
	}


	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if(files != null && files.length > 0) {
			try {
				MultipartFile arquivo =  files[0];
				novoNome = renomearArquivo(arquivo.getOriginalFilename());
				arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + FileSystems.getDefault().getSeparator() + novoNome));
			} catch (IOException e) {
				throw new NegocioException("Erro movendo foto para local temporário " + e);
			}
		}
		return novoNome;
	}
	
	@Override
	public byte[] recuperarFotoLocal(String nomeFoto) {
		try {
			return Files.readAllBytes(local.resolve(nomeFoto));
		} catch (IOException e) {
			throw new NegocioException("Erro recuperando foto do local temporário");
		}
	}
	@Override
	public byte[] recuperarFotoTemporaria(String nomeFoto) {
		try {
			return Files.readAllBytes(localTemporario.resolve(nomeFoto));
		} catch (IOException e) {
			throw new NegocioException("Erro recuperando foto do local temporario");
		}
	}
	
	@Override
	public void salvarFoto(String nomeFoto) {
		try {
			Files.move(this.localTemporario.resolve(nomeFoto), this.local.resolve(nomeFoto));
			
			removerArquivoTemp(nomeFoto);
		} catch (IOException e) {
			throw new NegocioException("Erro movendo foto para o distino final" + e);
		}
		
		try {
			Thumbnails.of(this.local.resolve(nomeFoto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new NegocioException("Erro gerando thumbnails");
		}
	}
	
	@Override
	public byte[] recuperarThumbnail(String foto) {
		return recuperarFotoLocal("thumbnail." + foto);
	}
	
	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = FileSystems.getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("pastas criadas para salvar fotos");
				LOGGER.debug("Pastas default " + this.local.toAbsolutePath());
				LOGGER.debug("Pasta temporária " + this.localTemporario.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new NegocioException("Erro criando diretório para fotos" + e);
		}
	}
	
	private String renomearArquivo(String nome) {
		return String.format("%2$s-%1$s", nome, UUID.randomUUID().toString());
	}

	private void removerArquivoTemp(String foto) {
		try {
			Files.deleteIfExists(localTemporario.resolve(foto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
