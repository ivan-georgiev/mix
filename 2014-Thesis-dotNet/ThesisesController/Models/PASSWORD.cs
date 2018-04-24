using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class PASSWORD
    {
        public int ID { get; set; }
        public int OFFICIAL_ID { get; set; }
        public string PASS { get; set; }
        public virtual OFFICIAL OFFICIAL { get; set; }
    }
}
