package archivo;

import modelo.*;
import java.util.ArrayList;

public class DatosIniciales {

    public static ArrayList<Producto> cargar() {
        ArrayList<Producto> lista = new ArrayList<>();
 lista.add(new ProductoAbarrotes("ABA001","Arroz Extra 1 kg",        22.50, 50, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/White-rice.jpg/200px-White-rice.jpg",
            "Arroz grano largo, cocción rápida"));
        lista.add(new ProductoAbarrotes("ABA002","Frijol Negro 1 kg",       28.00, 40, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Feij%C3%A3o_preto.jpg/200px-Feij%C3%A3o_preto.jpg",
            "Frijol negro de primera calidad"));
        lista.add(new ProductoAbarrotes("ABA003","Aceite Vegetal 1 L",      49.00, 35, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Cooking_oil.jpg/200px-Cooking_oil.jpg",
            "Aceite vegetal sin colesterol"));
        lista.add(new ProductoAbarrotes("ABA004","Azúcar Estándar 1 kg",    26.50, 60, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Azucar_blanquilla.jpg/200px-Azucar_blanquilla.jpg",
            "Azúcar blanca refinada"));
        lista.add(new ProductoAbarrotes("ABA005","Harina de Trigo 1 kg",    24.00, 45, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Flour_in_bowl.jpg/200px-Flour_in_bowl.jpg",
            "Harina todo uso"));
        lista.add(new ProductoAbarrotes("ABA006","Pasta Espagueti 500 g",   19.00, 55, 0.5,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Spaghetti.jpg/200px-Spaghetti.jpg",
            "Pasta de sémola de trigo"));
        lista.add(new ProductoAbarrotes("ABA007","Sal de Mesa 1 kg",        12.00, 70, 1.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Salt_shaker_on_white_background.jpg/200px-Salt_shaker_on_white_background.jpg",
            "Sal yodada con flúor"));
        lista.add(new ProductoAbarrotes("ABA008","Atún en Lata 170 g",      33.00, 80, 0.17,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Dose_thunfisch.jpg/200px-Dose_thunfisch.jpg",
            "Atún en agua, 170 g"));

        // ── 2. BEBIDAS ────────────────────────────────────────────────────
        lista.add(new ProductoBebidas("BEB001","Agua Natural 1.5 L",        14.00,100,1500, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6a/Plastic_bottle_water.jpg/180px-Plastic_bottle_water.jpg",
            "Agua purificada sin gas"));
        lista.add(new ProductoBebidas("BEB002","Refresco Cola 2 L",         32.00, 60,2000, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f2/Coca-Cola_bottle_2l.jpg/180px-Coca-Cola_bottle_2l.jpg",
            "Refresco de cola"));
        lista.add(new ProductoBebidas("BEB003","Jugo de Naranja 1 L",       28.00, 40,1000, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/11/Orangejuice.jpg/180px-Orangejuice.jpg",
            "Jugo 100% naranja"));
        lista.add(new ProductoBebidas("BEB004","Bebida Energética 355 ml",  35.00, 50, 355, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/RedBull_Logo.svg/180px-RedBull_Logo.svg.png",
            "Bebida con cafeína y taurina"));
        lista.add(new ProductoBebidas("BEB005","Café Soluble 100 g",        55.00, 30, 100, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/A_small_cup_of_coffee.JPG/180px-A_small_cup_of_coffee.JPG",
            "Café instantáneo premium"));
        lista.add(new ProductoBebidas("BEB006","Cerveza Clara 355 ml",      22.00,120, 355, true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Beer_bottle_and_glass.jpg/180px-Beer_bottle_and_glass.jpg",
            "Cerveza tipo lager"));
        lista.add(new ProductoBebidas("BEB007","Té Verde Caja 25 sobres",   38.00, 35, 500, false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Green_tea.jpg/180px-Green_tea.jpg",
            "Té verde antioxidante, 25 sobres"));

        // ── 3. LÁCTEOS Y HUEVO ────────────────────────────────────────────
        lista.add(new ProductoLacteos("LAC001","Leche Entera 1 L",          24.00, 80, 5,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Milk_glass.jpg/180px-Milk_glass.jpg",
            "Leche UHT entera"));
        lista.add(new ProductoLacteos("LAC002","Huevo Blanco 12 pzas",      52.00, 60,10,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/26/YellowEggsOnWhite.jpg/200px-YellowEggsOnWhite.jpg",
            "Huevo de gallina fresco"));
        lista.add(new ProductoLacteos("LAC003","Yogurt Natural 1 kg",       46.00, 35, 7,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Caramelized_Yogurt.jpg/200px-Caramelized_Yogurt.jpg",
            "Yogurt griego sin azúcar"));
        lista.add(new ProductoLacteos("LAC004","Mantequilla 90 g",          32.00, 40,14,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/Butter_and_salt.jpg/200px-Butter_and_salt.jpg",
            "Mantequilla con sal"));
        lista.add(new ProductoLacteos("LAC005","Crema Ácida 200 ml",        22.00, 45, 7,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Smetana.jpg/200px-Smetana.jpg",
            "Crema ácida 20% grasa"));
        lista.add(new ProductoLacteos("LAC006","Gelatina de Fresa 170 g",   18.00, 50,30,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Red_Jello.jpg/200px-Red_Jello.jpg",
            "Gelatina en polvo, sabor fresa"));
 lista.add(new ProductoFrutasVerduras("FRU001","Manzana Roja",       35.00, 30,"kg","Otoño",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Red_Apple.jpg/180px-Red_Apple.jpg",
            "Manzana red delicious por kilo"));
        lista.add(new ProductoFrutasVerduras("FRU002","Plátano Tabasco",    18.00, 25,"kg","Todo el año",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Banana-Fruit.jpg/180px-Banana-Fruit.jpg",
            "Plátano maduro por kilo"));
        lista.add(new ProductoFrutasVerduras("FRU003","Tomate Saladet",     22.00, 40,"kg","Todo el año",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Tomato_je.jpg/180px-Tomato_je.jpg",
            "Tomate bola por kilo"));
        lista.add(new ProductoFrutasVerduras("FRU004","Cebolla Blanca",     20.00, 35,"kg","Todo el año",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Onions.jpg/180px-Onions.jpg",
            "Cebolla blanca por kilo"));
        lista.add(new ProductoFrutasVerduras("FRU005","Chile Serrano",      45.00, 20,"kg","Todo el año",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Serrano_peppers.jpg/180px-Serrano_peppers.jpg",
            "Chile serrano fresco por kilo"));
        lista.add(new ProductoFrutasVerduras("FRU006","Lechuga Romana",     18.00, 25,"pieza","Todo el año",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Lettuce_heart.jpg/180px-Lettuce_heart.jpg",
            "Lechuga romana por pieza"));

        lista.add(new ProductoCarnesPescados("CAR001","Pechuga de Pollo",   88.00, 20,"Filete",false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Chicken_breast.jpg/180px-Chicken_breast.jpg",
            "Pechuga de pollo fresca por kilo"));
        lista.add(new ProductoCarnesPescados("CAR002","Carne Molida Res",  120.00, 15,"Molida",false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Ground_beef_1.jpg/180px-Ground_beef_1.jpg",
            "Carne molida 90/10 por kilo"));
        lista.add(new ProductoCarnesPescados("CAR003","Costilla de Cerdo", 110.00, 12,"Tira",false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ed/Pork_spare_ribs.jpg/180px-Pork_spare_ribs.jpg",
            "Costilla de cerdo por kilo"));
        lista.add(new ProductoCarnesPescados("CAR004","Filete de Tilapia",  95.00, 18,"Filete",true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Tilapia.jpg/180px-Tilapia.jpg",
            "Tilapia congelada por kilo"));
        lista.add(new ProductoCarnesPescados("CAR005","Camarón Mediano",   220.00, 10,"Entero",true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/White_shrimp.jpg/180px-White_shrimp.jpg",
            "Camarón sin cabeza congelado por kilo"));

        lista.add(new ProductoSalchichoneria("SAL001","Jamón de Pierna",    95.00, 15,true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c4/Cooked_ham.jpg/180px-Cooked_ham.jpg",
            "Jamón de pierna por kilo"));
        lista.add(new ProductoSalchichoneria("SAL002","Salchicha Viena 250 g",42.00,30,false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Frankfurter_Wuerstchen.jpg/180px-Frankfurter_Wuerstchen.jpg",
            "Salchichas tipo viena, paq 250 g"));
        lista.add(new ProductoSalchichoneria("SAL003","Tocino Ahumado 200 g",55.00,25,false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Bacons.jpg/180px-Bacons.jpg",
            "Tocino ahumado rebanado"));
        lista.add(new ProductoSalchichoneria("SAL004","Queso Manchego",    130.00, 10,true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Cheese.jpg/180px-Cheese.jpg",
            "Queso manchego por kilo"));
        lista.add(new ProductoSalchichoneria("SAL005","Chorizo Rojo 250 g", 48.00, 20,false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Chorizo.jpg/180px-Chorizo.jpg",
            "Chorizo español 250 g"));
  lista.add(new ProductoPanaderia("PAN001","Pan de Caja Blanco",      38.00, 40,20,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/Fresh_made_bread_05.jpg/180px-Fresh_made_bread_05.jpg",
            "Pan de caja blanco 20 rebanadas"));
        lista.add(new ProductoPanaderia("PAN002","Bolillo (pieza)",          4.50,200, 1,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Bolillos.jpg/180px-Bolillos.jpg",
            "Bolillo recién horneado"));
        lista.add(new ProductoPanaderia("PAN003","Tortilla de Maíz 1 kg",   22.00, 60,36,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/77/Tortillas.jpg/180px-Tortillas.jpg",
            "Tortillas de maíz azul/blanco"));
        lista.add(new ProductoPanaderia("PAN004","Pan Dulce Concha",         8.50,100, 1,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Concha_bread.jpg/180px-Concha_bread.jpg",
            "Concha de mantequilla"));
        lista.add(new ProductoPanaderia("PAN005","Tortilla de Harina 10 pzas",26.00,45,10,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/Flour_tortillas.jpg/180px-Flour_tortillas.jpg",
            "Tortillas de harina 10 piezas"));
    lista.add(new ProductoLimpieza("LIM001","Detergente en Polvo 1 kg", 62.00, 30,true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Powder_detergent.jpg/180px-Powder_detergent.jpg",
            "Detergente multiusos en polvo"));
        lista.add(new ProductoLimpieza("LIM002","Suavizante Ropa 1 L",      45.00, 25,true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Fabric_softener.jpg/180px-Fabric_softener.jpg",
            "Suavizante con fragancia primavera"));
        lista.add(new ProductoLimpieza("LIM003","Desinfectante Pino 1 L",   38.00, 35,true,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Cleaning_supplies.jpg/180px-Cleaning_supplies.jpg",
            "Desinfectante de pisos aroma pino"));
        lista.add(new ProductoLimpieza("LIM004","Papel Higiénico 4 rollos", 52.00, 50,false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/White_shark_Toilet_paper.jpg/180px-White_shark_Toilet_paper.jpg",
            "Papel higiénico doble hoja 4 rollos"));
        lista.add(new ProductoLimpieza("LIM005","Esponja de Cocina",        12.00, 60,false,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/Sponge_for_dishes.jpg/180px-Sponge_for_dishes.jpg",
            "Esponja con fibra amarilla y verde"));
    lista.add(new ProductoCuidadoPersonal("CUI001","Shampú 400 ml",     58.00, 25,"Todo tipo",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Shampoo_bottle.jpg/180px-Shampoo_bottle.jpg",
            "Shampú hidratante"));
        lista.add(new ProductoCuidadoPersonal("CUI002","Jabón de Tocador 90 g",14.00,60,"Seco",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Dove-Soap.jpg/180px-Dove-Soap.jpg",
            "Jabón barra hidratante"));
        lista.add(new ProductoCuidadoPersonal("CUI003","Pasta Dental 100 ml",32.00,40,"Todo tipo",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Toothpaste_3.jpg/180px-Toothpaste_3.jpg",
            "Pasta dental flúor triple acción"));
        lista.add(new ProductoCuidadoPersonal("CUI004","Desodorante Spray 150 ml",55.00,30,"Normal",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Deodorant.jpg/180px-Deodorant.jpg",
            "Desodorante 48h protección"));
        lista.add(new ProductoCuidadoPersonal("CUI005","Crema Corporal 200 ml",75.00,20,"Seco",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Body_lotion.jpg/180px-Body_lotion.jpg",
            "Crema nutritiva piel seca"));
     lista.add(new ProductoSnacks("SNA001","Papas Fritas 110 g",         22.00, 80,"Sal",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Potato_chips.jpg/180px-Potato_chips.jpg",
            "Papas lisas saladas"));
        lista.add(new ProductoSnacks("SNA002","Galletas de Chocolate 400 g",35.00, 50,"Chocolate",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Choco_chip_cookie.jpg/180px-Choco_chip_cookie.jpg",
            "Galletas con chispas de chocolate"));
        lista.add(new ProductoSnacks("SNA003","Chocolate con Leche 90 g",   28.00, 60,"Leche",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Chocolate_bar.jpg/180px-Chocolate_bar.jpg",
            "Tablilla de chocolate con leche"));
        lista.add(new ProductoSnacks("SNA004","Gomitas de Frutas 100 g",    18.00, 70,"Frutas",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Gummy_bears.jpg/180px-Gummy_bears.jpg",
            "Gomitas multicolor surtidas"));
        lista.add(new ProductoSnacks("SNA005","Cacahuates Enchilados 100 g",15.00, 90,"Picante",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Peanuts.jpg/180px-Peanuts.jpg",
            "Cacahuates con chile y limón"));

        lista.add(new ProductoMascotas("MAS001","Croquetas para Perro 5 kg",285.00,15,"Perro",5.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Dry_dog_food.jpg/180px-Dry_dog_food.jpg",
            "Alimento seco adulto mediano"));
        lista.add(new ProductoMascotas("MAS002","Croquetas para Gato 3 kg",210.00,12,"Gato",3.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Cat_food.jpg/180px-Cat_food.jpg",
            "Alimento seco gato adulto"));
        lista.add(new ProductoMascotas("MAS003","Alimento Húmedo Perro lata",35.00,40,"Perro",0.35,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Dog_food_canned.jpg/180px-Dog_food_canned.jpg",
            "Lata comida húmeda perro 350 g"));
        lista.add(new ProductoMascotas("MAS004","Arena para Gato 5 kg",     95.00, 20,"Gato",5.0,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/Cat_litter.jpg/180px-Cat_litter.jpg",
            "Arena aglomerante sin polvo"));
        lista.add(new ProductoMascotas("MAS005","Collar Ajustable Perro",   58.00, 25,"Perro",0.1,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Dog_collar.jpg/180px-Dog_collar.jpg",
            "Collar nylon talla M"));
        lista.add(new ProductoMascotas("MAS006","Juguete para Gato",        42.00, 30,"Gato",0.05,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Cat_toy.jpg/180px-Cat_toy.jpg",
            "Varilla con plumas interactiva"));

        return lista;
    }
}
