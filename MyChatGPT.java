package com.netbox;
import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import java.util.Scanner;
public class MyChatGPT {

    // 定义一个公共静态方法quickStart，它可能抛出三种异常：ApiException，NoApiKeyException和InputRequiredException
    public static void quickStart() throws ApiException, NoApiKeyException, InputRequiredException {

        // 创建一个Conversation对象，这是与AI模型通信的接口
        Conversation conversation = new Conversation();

        // 打印提示信息，告诉用户可以开始提问
        System.out.println("请提问:");

        // 创建一个Scanner对象来读取用户的输入
        Scanner scanner = new Scanner(System.in);

        // 读取用户输入的一行字符串作为问题
        String prompt = scanner.nextLine(); // 注意：这里应该是nextLine()而不是next()，因为next()只读取单词

        // 使用Builder模式构建一个ConversationParam对象，包含模型类型、问题等参数
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_TURBO) // 指定使用的AI模型为Qwen Turbo
                .prompt(prompt) // 将用户的问题设置为prompt
                .build(); // 构建并返回ConversationParam对象

        // 调用conversation对象的call方法，传入参数，获取结果
        ConversationResult result = conversation.call(param);

        // 打印回答的前缀
        System.out.println("回答：");

        // 打印AI模型的回答
        System.out.println(result.getOutput().getText());
    }

    // 主方法，程序的入口点
    public static void main(String[] args) {

        // 无限循环，允许用户连续提问
        try {
            while (true) {
                quickStart();
            }
        }
        // 捕获并处理可能出现的异常
        catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 打印异常信息
            System.out.println(e.getMessage());
        }

        // 结束程序
        System.exit(0);
    }
}