package com.awbp;

import java.io.File;

public interface TelegramSender {
    public void sendMessage(Long chatId, String text);
    public void sendVideo(Long chatId, File file);
    public void sendPhoto(Long chatId, File file);
}
