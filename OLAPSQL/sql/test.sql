

set serverout on

   select * from meta_element order by typ;
   select * from meta_star;
   select * from meta_hierarchy;
   select * from meta_attribute;
   select * from meta_measure;
   select * from meta_level;

   --insert into produits values (4,34, 'agrafe', 'outils','b');

   --insert into produits values (2,34, 'agrafeuse', 'outils', 'b');
   
   --insert into produits values (3,34, 'arrache-agrafe', 'utils',' b');

   --insert into produits values (4,34, 'agrafeurr', 'outils', 'b');

   select * from produits;
   --select seq_D_produits.nextval from dual;
   --delete from meta_element;
   --delete from meta_star;
   --delete from meta_hierarchy;
   --delete from meta_attribute;
   --delete from meta_measure;
   --delete from meta_level; */


DECLARE
    temp  meta_element.id%type:=3;
    --temp2  meta_attribute.ida%type;
BEGIN
     DBMS_OUTPUT.PUT_LINE('debut');
     --temp:=GEST_BASE_3D.GET_ID_ELMT('produits', 'D');
    --temp2 :=   GEST_BASE_3D.CREATE_ATT(temp, 'attdate', 0,null,null);

    --DBMS_OUTPUT.PUT_LINE(temp);
END;
/
 --select * from meta_element;
--execute GEST_BASE_3D.DROP_FACT('ventes');
--SELECT me2.name from meta_star ms, meta_element me1, meta_element me2
--WHERE me1.name= 'ventes' and me1.typ='F' and me1.id=ms.idf and
 --me2.id=ms.idd;
   
   desc ventes;
--rollback;
--commit;
 --EXEMPLE CURSOR
  --CURSOR c_att IS
         --SELECT idm FROM meta_measure WHERE idf = '1' --GET_ID_ELMT(nomtable,t) ;
   --my_rec  c_att%ROWTYPE;

        --OPEN c_att;
         --DBMS_OUTPUT.PUT_LINE(c_att%COUNT);
        --LOOP
            --FETCH c_att INTO my_rec;
           --EXIT WHEN c_att%NOTFOUND;

           --delete from meta_measure where idm = my_rec.idm;
           --delete from meta_attribute WHERE ida = my_rec.idm;

        --END LOOP;
        --CLOSE c_att;

--execute GEST_BASE_3D.GET_ID_ELMT('ventes','F' );
