using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Commons
{
    [Serializable]
   public class ChatMessage
    {
        private String senderName;
        private String receiverName;
        private String message;

        public String SenderName
        {
            get
            {
                return senderName;
            }
        }

        public String ReceiverName
        {
            get
            {
                return receiverName;
            }
        }
        public String Message
        {
            get
            {
                return message;
            }
        }

        public ChatMessage(String senderName, String receiverName, String message)
        {
            this.senderName = senderName;
            this.receiverName = receiverName;
            this.message = message;
        }

    }

}
