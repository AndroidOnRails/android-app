package com.hana053.micropost.ui.pages.usershow;

import com.hana053.micropost.domain.Micropost;
import com.hana053.micropost.interactors.UserMicropostInteractor;
import com.hana053.micropost.testing.RobolectricBaseTest;
import com.hana053.micropost.ui.components.micropostlist.PostListAdapter;

import org.junit.Test;

import java.util.List;

import rx.Observable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserShowServiceTest extends RobolectricBaseTest {

    private final UserMicropostInteractor userMicropostInteractor = mock(UserMicropostInteractor.class);
    private final PostListAdapter postAdapter = new PostListAdapter();
    private final UserShowService userShowService = new UserShowServiceImpl(userMicropostInteractor, postAdapter);

    @Test
    public void shouldLoadPosts() {
        final Observable<List<Micropost>> dummyPosts = Observable
                .just(new Micropost(1, "content", 0, null))
                .toList();
        when(userMicropostInteractor.loadPrevPosts(1, null)).thenReturn(dummyPosts);

        userShowService.loadPosts(1).subscribe();
        advance();

        assertThat(postAdapter.getItemCount(), is(1));
    }

}