import React, { useState, useEffect, useRef } from 'react';
import './ChatBox.css';
import { sendMessage } from '../services/api';

function ChatBox() {
  const [messages, setMessages] = useState([
    {
      text: "你好！我是你的 AI 助手。有什么我可以帮你的吗？",
      sender: 'ai'
    }
  ]);
  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef(null);

  const recommendedQuestions = [
    {
      title: "常见问题",
      questions: [
        "你能做什么？",
        "你是如何工作的？",
        "你的知识范围是什么？"
      ]
    },
    {
      title: "技术问题",
      questions: [
        "如何学习编程？",
        "Python 和 JavaScript 的区别？",
        "什么是机器学习？"
      ]
    },
    {
      title: "其他问题",
      questions: [
        "如何提高工作效率？",
        "怎样学习一门新技能？",
        "如何进行时间管理？"
      ]
    }
  ];

  const scrollToBottom = () => {
    if (messagesEndRef.current) {
      const container = messagesEndRef.current.parentElement;
      container.scrollTop = container.scrollHeight;
    }
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const typeMessage = async (text, callback) => {
    setIsTyping(true);
    const words = text.split(' ');
    let currentText = '';
    
    for (let word of words) {
      currentText += word + ' ';
      callback(currentText.trim());
      await new Promise(resolve => setTimeout(resolve, 30));
    }
    setIsTyping(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    // Add user message
    const userMessage = {
      text: input,
      sender: 'user'
    };
    setMessages(prevMessages => [...prevMessages, userMessage]);
    setInput('');

    try {
      setIsTyping(true);
      const tempMessage = { text: '', sender: 'ai' };
      setMessages(prevMessages => [...prevMessages, tempMessage]);

      const response = await sendMessage([...messages, userMessage]);
      
      typeMessage(response, (text) => {
        setMessages(prevMessages => [
          ...prevMessages.slice(0, -1),
          { ...tempMessage, text }
        ]);
      });
    } catch (error) {
      setMessages(prevMessages => [
        ...prevMessages,
        { text: "抱歉，发生了一些错误。请稍后再试。", sender: 'ai' }
      ]);
      setIsTyping(false);
    }
  };

  const handleQuestionClick = (question) => {
    setInput(question);
  };

  return (
    <div className="chat-container">
      <div className="chat-box">
        <div className="chat-header">
          <h2>AI 智能助手</h2>
          {isTyping && <div className="typing-indicator">AI 正在输入...</div>}
        </div>
        <div className="messages-container">
          {messages.map((message, index) => (
            <div key={index} className={`message ${message.sender}`}>
              <div className="message-content">{message.text}</div>
            </div>
          ))}
          <div ref={messagesEndRef} />
        </div>
        <form onSubmit={handleSubmit} className="input-form">
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="请输入您的问题..."
            className="chat-input"
          />
          <button type="submit" className="send-button">
            发送
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M22 2L11 13" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            </svg>
          </button>
        </form>
      </div>
      <div className="recommended-questions">
        {recommendedQuestions.map((category, index) => (
          <div key={index} className="question-category">
            <h3>{category.title}</h3>
            <div className="question-list">
              {category.questions.map((question, qIndex) => (
                <div
                  key={qIndex}
                  className="question-card"
                  onClick={() => handleQuestionClick(question)}
                >
                  {question}
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ChatBox; 