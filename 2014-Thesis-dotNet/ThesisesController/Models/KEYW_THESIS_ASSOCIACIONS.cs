using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class KEYW_THESIS_ASSOCIACIONS
    {
        public int ID { get; set; }
        public int THESIS_ID { get; set; }
        public int KEYWORD_ID { get; set; }
        public virtual KEYWORD KEYWORD { get; set; }
        public virtual THESIS THESIS { get; set; }
    }
}
