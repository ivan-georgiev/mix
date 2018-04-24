using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Commons
{
    [Serializable]
    public class ChatUserControl
    {
        String userName;
        Boolean active;

        public String UserName
        {
            get
            {
                return userName;
            }
        }

        public Boolean Active
        {
            get
            {
                return active;
            }
        }

        public ChatUserControl(String userName, Boolean active)
        {
            this.userName = userName;
            this.active = active;
        }

    }
}
