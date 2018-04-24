using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class DOCUMENT_TYPES
    {
        public DOCUMENT_TYPES()
        {
            this.DOCUMENTS = new List<DOCUMENT>();
        }

        public int ID { get; set; }
        public string NAME { get; set; }
        public string DESCRIPTION { get; set; }
        public virtual ICollection<DOCUMENT> DOCUMENTS { get; set; }
    }
}
