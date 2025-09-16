package com.linzi.daily.ai.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@Configurable
public class ChatClientConfig {

    @Autowired
    private ToolCallbackProvider tools;
    ChatModel chatModel;

    @Bean
    public CommandLineRunner predefinedQuestions(ConfigurableApplicationContext context){
        return args -> {
            // 构建ChatClient,此时不注入任何工具
            var chatClient = ChatClient.builder(chatModel).defaultTools((Object) tools.getToolCallbacks()).build();
            String userInput = "帮我将这个网页内容进行抓取 https://www.shuaijiao.cn/news/view/68320.html";
            System.out.println(">>> QUESTION: "+ userInput);
            System.out.println(">>> ASSISTANT: " + chatClient.prompt().user(userInput).call().content());
            context.close();
        };
    }
}
