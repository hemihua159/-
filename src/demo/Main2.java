package demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

//обтьнд╪Ч
public class Main2 {
	private final static String bucketName = "hemihua";
	private final static String filePath   = 
          "C:\\Users\\nxhmh\\Desktop\\download\\hello.txt";
	private final static String accessKey = "4C1D5EF21C436099F7AA";
private final static String secretKey = 
"WzlBQjA2Q0VGNTFBRTU0RDYzMEE5NDVBOUY1RDY0NEVCRENFNjczRUJd";
	private final static String serviceEndpoint = 
		"http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	public static void main(String[] args) {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().
				withUseExpectContinue(true);

		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();

        final String keyName = Paths.get(filePath).getFileName().toString();
        final File file = new File(filePath);

        System.out.format("Downloading %s to S3 bucket %s...\n", keyName, bucketName);
        
        S3ObjectInputStream s3is = null;
		FileOutputStream fos = null;
		try {
			S3Object o = s3.getObject(bucketName, keyName);
			s3is = o.getObjectContent();
			fos = new FileOutputStream(new File(filePath));
			byte[] read_buf = new byte[64 * 1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
		} catch (AmazonServiceException e) {
			System.err.println(e.toString());
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} finally {
			if (s3is != null) try { s3is.close(); } catch (IOException e) { }
			if (fos != null) try { fos.close(); } catch (IOException e) { }
		}
		
		System.out.println("Done!");
	}
}