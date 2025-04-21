import React, { useState, useEffect, useRef } from 'react';
import './ChatBox.css';

function ChatBox() {
  const [messages, setMessages] = useState([
    {
      text: "Hello! I'm your fitness and nutrition AI assistant. How can I help you today?",
      sender: 'ai'
    }
  ]);
  const [input, setInput] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef(null);

  const recommendedQuestions = [
    {
      title: "Workout Planning",
      questions: [
        "What's the best workout routine for beginners?",
        "How to build muscle effectively?",
        "Recommend cardio exercises for weight loss"
      ]
    },
    {
      title: "Nutrition Advice",
      questions: [
        "What should I eat before and after workout?",
        "How to plan a balanced diet?",
        "Protein-rich vegetarian food options"
      ]
    },
    {
      title: "Fitness Goals",
      questions: [
        "How to stay motivated during workouts?",
        "Tips for maintaining workout consistency",
        "Setting realistic fitness goals"
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
      await new Promise(resolve => setTimeout(resolve, 50)); // 加快打字速度
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

    // Simulate AI response with typing effect
    const aiResponse = "I understand you're interested in fitness and nutrition. Let me help you with that. What specific aspects would you like to know more about?";
    const tempMessage = { text: '', sender: 'ai' };
    
    setTimeout(() => {
      setMessages(prevMessages => [...prevMessages, tempMessage]);
      typeMessage(aiResponse, (text) => {
        setMessages(prevMessages => [
          ...prevMessages.slice(0, -1),
          { ...tempMessage, text }
        ]);
      });
    }, 500); // 添加短暂延迟使用户消息可见
  };

  const handleQuestionClick = (question) => {
    setInput(question);
  };

  return (
    <div className="chat-container">
      <div className="chat-box">
        <div className="chat-header">
          <h2>Fitness & Nutrition AI Assistant</h2>
          {isTyping && <div className="typing-indicator">AI is typing...</div>}
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
            placeholder="Ask about fitness advice or recipe recommendations..."
            className="chat-input"
          />
          <button type="submit" className="send-button">
            Send
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