using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class KEYW_THESIS_ASSOCIACIONSMap : EntityTypeConfiguration<KEYW_THESIS_ASSOCIACIONS>
    {
        public KEYW_THESIS_ASSOCIACIONSMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            // Table & Column Mappings
            this.ToTable("KEYW_THESIS_ASSOCIACIONS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.THESIS_ID).HasColumnName("THESIS_ID");
            this.Property(t => t.KEYWORD_ID).HasColumnName("KEYWORD_ID");

            // Relationships
            this.HasRequired(t => t.KEYWORD)
                .WithMany(t => t.KEYW_THESIS_ASSOCIACIONS)
                .HasForeignKey(d => d.KEYWORD_ID);
            this.HasRequired(t => t.THESIS)
                .WithMany(t => t.KEYW_THESIS_ASSOCIACIONS)
                .HasForeignKey(d => d.THESIS_ID);

        }
    }
}
