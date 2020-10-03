package com.betharley.wesleycatula.mobile.appemprego;

import android.content.Context;

public class Json_carregar {
    public String JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=1";

    private String url;
    private Context context;
    public Json_carregar(String url, Context context) {
        this.context = context;
        this.url = url;
    }

    public void jsonrequest() {

        /*
        context.request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Postagem postagem = new Postagem();

                        //TITULO
                        JSONObject jsonObjectTitle = jsonObject.getJSONObject("title");
                        postagem.setTitle( jsonObjectTitle.getString("rendered") );

                        //CONTEUDO
                        //postagem.setSource_url( jsonObject.getString("source_url") );
                        JSONObject jsonObjectConteudo = jsonObject.getJSONObject("content");
                        postagem.setContent( jsonObjectConteudo.getString("rendered") );

                        //DESTAQUE
                        JSONObject jsonObjectDestaque = jsonObject.getJSONObject("excerpt");
                        postagem.setExcerpt( jsonObjectDestaque.getString("rendered") );

                        //DATA
                        postagem.setData( jsonObject.getString("date") );

                        //IMAGEM
                        postagem.setSource_url( jsonObject.getString("jetpack_featured_media_url") );
*/
                        /*postagem.setName(jsonObject.getString("name"));
                        postagem.setDescription(jsonObject.getString("description"));
                        postagem.setRating(jsonObject.getString("Rating"));
                        postagem.setEpisode(jsonObject.getInt("episode"));
                        postagem.setCategory(jsonObject.getString("categorie"));
                        postagem.setStudio(jsonObject.getString("studio"));
                        postagem.setImage_url(jsonObject.getString("img"));*/
/*
                        lista.add(postagem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //NOTIFICA O ADAPTADOR
                adaptadorPostagem.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(context.this);
        requestQueue.add(request);
        */
    }
}
