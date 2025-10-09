select * from self_service.tkt_ticket_attributes where ticket_id=54131

select * from self_service.tkt_ticket where id=54079

select * from self_service.tkt_ticket_transaction where ticket_id=54079

select * from self_service.tkt_ticket_attributes where value='555' and type_id=200;

desc self_service.tkt_ticket_attributes;

desc self_service.tkt_ticket_attributes;

select ticket_id from self_service.tkt_ticket_attributes a where a.value = '555' and a.type_id= 200

DELETE FROM self_service.tkt_ticket WHERE id=45777
DELETE FROM self_service.tkt_ticket_attributes WHERE ticket_id=45777

select * from self_service.tkt_ticket WHERE id=47992
select * FROM self_service.tkt_ticket_attributes WHERE ticket_id=47992

DELETE FROM "SELF_SERVICE"."TKT_ATTRIBUTES_LIST" WHERE ID=802
DELETE FROM "SELF_SERVICE"."TKT_ATTRIBUTES_LIST" WHERE ID=806
DELETE FROM "SELF_SERVICE"."TKT_ATTRIBUTES_LIST" WHERE ID=812

DELETE FROM self_service.tkt_attributes_type WHERE ID=302

select ticket_id from self_service.tkt_ticket_attributes a where  a.type_id= 302

select count (*) from self_service.tkt_ticket_attributes a where  a.type_id= 302

//DELETE FROM self_service.tkt_ticket_attributes WHERE ticket_id=56191
DELETE FROM self_service.tkt_ticket_attributes WHERE ticket_id in (select ticket_id from self_service.tkt_ticket_attributes a where  a.type_id= 302)
DELETE FROM self_service.tkt_ticket WHERE id=56191
DELETE FROM self_service.tkt_ticket WHERE id in (select ticket_id from self_service.tkt_ticket_attributes a where  a.type_id= 302)