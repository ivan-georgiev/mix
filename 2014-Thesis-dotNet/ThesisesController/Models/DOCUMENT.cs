using System;
using System.Collections.Generic;

namespace ThesesController.Models
{
    public partial class DOCUMENT
    {
        public int ID { get; set; }
        public Nullable<int> AUTHOR_ID { get; set; }
        public int THESIS_ID { get; set; }
        public int TYPE_ID { get; set; }
        public int SIZE { get; set; }
        public string FILE_EXTENSION { get; set; }
        public string DESCRIPTION { get; set; }
        public byte PUBLIC_ACCESS { get; set; }
        public byte[] BINCONTENT { get; set; }
        public Nullable<System.DateTime> DT_ADDED { get; set; }
        public virtual DOCUMENT_TYPES DOCUMENT_TYPES { get; set; }
        public virtual THESIS THESIS { get; set; }
        public virtual OFFICIAL OFFICIAL { get; set; }
    }
}
