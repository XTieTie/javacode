package com.netbox;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
public class MyChatImg {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final String MODEL = "flux-schnell";
    private static final String SIZE = "1024*1024";
    public static void basicCall() throws ApiException, NoApiKeyException, IOException {
        //每次调用都重新输入请求，等待执行完毕
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入需要完成的图片需求：");
        String prompt=scanner.nextLine();

        System.out.println("图片正在生成，请耐心等待...");
        //调用接口，生成图片
        ImageSynthesis is = new ImageSynthesis();
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(MyChatImg.MODEL)
                        .n(1)
                        .size(MyChatImg.SIZE)
                        .prompt(prompt)
                        .negativePrompt("garfield")
                        .build();

        ImageSynthesisResult result = is.call(param);
        System.out.println(result);
//保存图片在本地
        for(Map<String, String> item :result.getOutput().getResults()){
            String paths = new URL(item.get("url")).getPath();
            String[] parts = paths.split("/");
            String fileName = parts[parts.length-1];
            Request request = new Request.Builder()
                    .url(item.get("url"))
                    .build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Path file = Paths.get(fileName);
                Files.write(file, response.body().bytes());

                System.out.println("图片生成成功！");
                System.out.println("图片名称为："+fileName);
            }

        }

    }
    public static void main(String[] args){
        try{
            basicCall();
        }catch(ApiException|NoApiKeyException | IOException e){
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

}