package com.example.user.popularmovies;

/**
 * Created by user on 8/29/2015.
 */
  public class Movie implements Comparable<Movie> {
    private String title;
    private String date;
    private String poster_path ;
    private String overview ;
    private String id ;
    private double popularity;
    private double vote_avrage;

    @Override
    public int compareTo(Movie movie)
    {
        if (this.vote_avrage >= movie.vote_avrage)
            return 1;
        else
            return -1;
    }


    public void setTitle (String title)
    {
        this.title = title;
    }
    public void setOverview (String overview)
    {
        this.overview = overview;
    }
    public void setId (String id)
    {
        this.id = id;
    }
    public void setDate (String date)
    {
        this.date = date;
    }
    public void setPoster_path (String poster_path)
    {
        this.poster_path = poster_path;
    }
    public void setPopularity (double popularity)
    {
        this.popularity=popularity;
    }
    public void setVote_avrage (double vote_avrage)
    {
        this.vote_avrage=vote_avrage;
    }



    public String getTitle ()
    {
        return title;
    }
    public String getOverview ()
    {
        return overview;
    }
    public String getId ()
    {
        return id;
    }
    public String getDate ()
    {
        return date;
    }
    public String getPoster_path()
    {
        return poster_path;
    }
    public double getPopularity ()
    {
        return popularity;
    }
    public double getVote_avrage()
    {
        return vote_avrage;
    }


}
