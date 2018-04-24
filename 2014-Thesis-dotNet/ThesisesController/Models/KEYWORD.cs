using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class KEYWORD
    {
        public KEYWORD()
        {
            this.KEYW_THESIS_ASSOCIACIONS = new List<KEYW_THESIS_ASSOCIACIONS>();
        }

        public int ID { get; set; }
        public string WORD { get; set; }
        public virtual ICollection<KEYW_THESIS_ASSOCIACIONS> KEYW_THESIS_ASSOCIACIONS { get; set; }
    }
}
