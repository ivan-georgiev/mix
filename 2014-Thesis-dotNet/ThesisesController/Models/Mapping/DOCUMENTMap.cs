using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity.ModelConfiguration;

namespace ThesesController.Models.Mapping
{
    public class DOCUMENTMap : EntityTypeConfiguration<DOCUMENT>
    {
        public DOCUMENTMap()
        {
            // Primary Key
            this.HasKey(t => t.ID);

            // Properties
            this.Property(t => t.FILE_EXTENSION)
                .IsRequired()
                .IsFixedLength()
                .HasMaxLength(8);

            this.Property(t => t.DESCRIPTION)
                .IsFixedLength()
                .HasMaxLength(100);

            // Table & Column Mappings
            this.ToTable("DOCUMENTS");
            this.Property(t => t.ID).HasColumnName("ID");
            this.Property(t => t.AUTHOR_ID).HasColumnName("AUTHOR_ID");
            this.Property(t => t.THESIS_ID).HasColumnName("THESIS_ID");
            this.Property(t => t.TYPE_ID).HasColumnName("TYPE_ID");
            this.Property(t => t.SIZE).HasColumnName("SIZE");
            this.Property(t => t.FILE_EXTENSION).HasColumnName("FILE_EXTENSION");
            this.Property(t => t.DESCRIPTION).HasColumnName("DESCRIPTION");
            this.Property(t => t.PUBLIC_ACCESS).HasColumnName("PUBLIC_ACCESS");
            this.Property(t => t.BINCONTENT).HasColumnName("BINCONTENT");
            this.Property(t => t.DT_ADDED).HasColumnName("DT_ADDED");

            // Relationships
            this.HasRequired(t => t.DOCUMENT_TYPES)
                .WithMany(t => t.DOCUMENTS)
                .HasForeignKey(d => d.TYPE_ID);
            this.HasRequired(t => t.THESIS)
                .WithMany(t => t.DOCUMENTS)
                .HasForeignKey(d => d.THESIS_ID);
            this.HasOptional(t => t.OFFICIAL)
                .WithMany(t => t.DOCUMENTS)
                .HasForeignKey(d => d.AUTHOR_ID);

        }
    }
}
