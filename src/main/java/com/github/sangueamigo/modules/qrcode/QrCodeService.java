package com.github.sangueamigo.modules.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QrCodeService {

    private static final int LARGURA  = 300;
    private static final int ALTURA   = 300;

    // Gera imagem QR Code em Base64 a partir do token
    public String gerarQrCodeBase64(String token) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    token,
                    BarcodeFormat.QR_CODE,
                    LARGURA,
                    ALTURA
            );

            BufferedImage imagem = MatrixToImageWriter.toBufferedImage(matrix);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(imagem, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code.", e);
        }
    }
}