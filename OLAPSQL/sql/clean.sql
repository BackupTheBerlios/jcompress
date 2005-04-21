 drop table ventes;
 drop table testDim;
drop table produits;
 drop table dim1;

 drop sequence seq_D_produits;
  drop sequence seq_D_dim1;
  drop sequence seq_F_ventes;


 
 
      delete from meta_element;
      delete from meta_star;
      delete from meta_hierarchy;
      delete from meta_attribute;
      delete from meta_measure;
      delete from meta_level;
      
      commit;
